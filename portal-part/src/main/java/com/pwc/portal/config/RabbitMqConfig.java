package com.pwc.portal.config;

import com.pwc.mapper.OmsOrderMapper;
import com.pwc.mapper.OmsOrderOperateHistoryMapper;
import com.pwc.model.OmsOrderOperateHistory;
import com.pwc.portal.component.CancelOrderReceiver;
import com.pwc.portal.dao.RetryMessageDao;
import com.pwc.portal.domain.QueueEnum;
import com.pwc.portal.domain.RetryMessage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.core.*;
import org.springframework.amqp.rabbit.connection.CorrelationData;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.annotation.PostConstruct;
import java.util.Date;
import java.util.concurrent.Executor;

@Configuration
public class RabbitMqConfig {
    
    private static final Logger LOGGER = LoggerFactory.getLogger(RabbitMqConfig.class);
    @Autowired
    private RabbitTemplate rabbitTemplate;  // 注入 RabbitTemplate
    @Autowired
    private RetryMessageDao retryDao; // 自定义重试表Mapper
    @Autowired
    private OmsOrderMapper orderMapper;     // 订单表Mapper
    @Autowired
    private OmsOrderOperateHistoryMapper historyMapper; // 操作历史表Mapper
    @Autowired
    private Executor retryExecutor;         // 异步重试线程池
    
    @Bean
    public DirectExchange orderExchange() {
        return ExchangeBuilder
                .directExchange(QueueEnum.QUEUE_ORDER_CANCEL.getExchange())
                .durable(true)
                .build();
    }
    
    @Bean
    public DirectExchange orderTtlExchange() {
        return ExchangeBuilder
                .directExchange(QueueEnum.QUEUE_TTL_ORDER_CANCEL.getExchange())
                .durable(true)
                .build();
    }
    
    @Bean
    public Queue orderQueue() {
        return new Queue(QueueEnum.QUEUE_ORDER_CANCEL.getName());
    }
    
    @Bean
    public Queue orderTtlQueue() {
        return QueueBuilder
                .durable(QueueEnum.QUEUE_ORDER_CANCEL.getName())
                .withArgument("x-dead-letter-exchange", QueueEnum.QUEUE_ORDER_CANCEL.getExchange())
                .withArgument("x-dead-letter-routing-key", QueueEnum.QUEUE_ORDER_CANCEL.getRouteKey())
                .build();
    }
    
    @Bean
    public Binding orderBinding(DirectExchange orderExchange, Queue orderQueue) {
        return BindingBuilder
                .bind(orderQueue)
                .to(orderExchange)
                .with(QueueEnum.QUEUE_ORDER_CANCEL.getRouteKey());
    }
    
    @Bean
    public Binding orderTtlBinding(DirectExchange orderTtlExchange, Queue orderTtlQueue) {
        return BindingBuilder
                .bind(orderTtlQueue)
                .to(orderTtlExchange)
                .with(QueueEnum.QUEUE_TTL_ORDER_CANCEL.getRouteKey());
    }

    @PostConstruct
    public void initConfirmCallback() {
        rabbitTemplate.setConfirmCallback((correlationData, ack, cause) -> {
            if (correlationData == null) return;
            String messageId = correlationData.getId();

            if (ack) {
                // 1. 消息成功到达 Broker，更新重试表状态
                retryDao.updateStatus(messageId, 1);
                LOGGER.info("消息确认成功: messageId={}", messageId);
            } else {
                // 2. 消息发送失败处理
                LOGGER.error("消息确认失败: messageId={}, cause={}", messageId, cause);

                //解析订单ID
                Long orderId = Long.parseLong(messageId.split("_")[0]);

                // 更新订单状态为异常（假设状态码4表示异常）
                orderMapper.updateOrderStatus(orderId, 4);

                // 记录操作历史
                OmsOrderOperateHistory history = new OmsOrderOperateHistory();
                history.setOrderId(orderId);
                history.setOperateType("消息投递失败");
                history.setNote("原因: " + cause);
                history.setCreateTime(new Date());
                historyMapper.insert(history);

                // 异步重试（最大3次）
                retryExecutor.execute(() -> retryMessage(messageId));
            }
        });
    }

    /**
     * 异步重试
     */
    private void retryMessage(String messageId) {
        RetryMessage retryMsg = retryDao.selectByMessageId(messageId);
        if (retryMsg.getRetryCount() >= 3) {
            LOGGER.error("消息重试超过3次，人工介入: messageId={}", messageId);
            return;
        }
        try {
            // 重新发送消息（使用原有延迟时间）
            rabbitTemplate.convertAndSend(
                    QueueEnum.QUEUE_TTL_ORDER_CANCEL.getExchange(),
                    QueueEnum.QUEUE_TTL_ORDER_CANCEL.getRouteKey(),
                    retryMsg.getOrderId(),
                    message -> {
                        message.getMessageProperties().setExpiration(
                                String.valueOf(retryMsg.getDelayTimes())
                        );
                        return message;
                    },
                    new CorrelationData(messageId) // 携带原消息ID
            );
            retryDao.incrementRetryCount(messageId);
        } catch (Exception e) {
            LOGGER.error("消息重试失败: messageId={}", messageId, e);
        }
    }

}
