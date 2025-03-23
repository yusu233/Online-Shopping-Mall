package com.pwc.portal.component;

import com.pwc.portal.dao.RetryMessageDao;
import com.pwc.portal.domain.QueueEnum;
import com.pwc.portal.domain.RetryMessage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.AmqpException;
import org.springframework.amqp.core.AmqpTemplate;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.core.MessageDeliveryMode;
import org.springframework.amqp.core.MessagePostProcessor;
import org.springframework.amqp.rabbit.connection.CorrelationData;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;
import org.springframework.transaction.support.TransactionSynchronization;
import org.springframework.transaction.support.TransactionSynchronizationManager;

import javax.annotation.PostConstruct;
import java.util.Date;

@Component
public class CancelOrderSender {
    private static final Logger LOGGER = LoggerFactory.getLogger(CancelOrderSender.class);
    @Autowired
    private RabbitTemplate rabbitTemplate;
    @Autowired
    private RetryMessageDao retryMessageDao;

    /**
     * 发送消息入口方法（供业务层调用）
     */
    public void sendMessage(Long orderId, long delayTimes) {
        // 1. 生成唯一消息ID（建议格式：orderId + 时间戳）
        String messageId = generateMessageId(orderId);

        // 2. 预写入重试表（状态=0，表示消息已发送但未确认）
        RetryMessage retryMsg = new RetryMessage();
        retryMsg.setOrderId(orderId);
        retryMsg.setMessageId(messageId);
        retryMsg.setDelayTimes(delayTimes);
        retryMsg.setStatus(0);
        retryMsg.setCreateTime(new Date());
        retryMessageDao.insert(retryMsg);

        // 3. 发送消息（确保在事务提交后执行）
        TransactionSynchronizationManager.registerSynchronization(
                new TransactionSynchronization() {
                    @Override
                    public void afterCommit() {
                        sendRabbitMessage(orderId, delayTimes, messageId);
                    }
                }
        );
    }
    
    private void sendRabbitMessage(Long orderId, long delayTimes, String messageId) {
        try {
            CorrelationData correlationData = new CorrelationData(messageId);

            // 发送消息（持久化 + TTL）
            rabbitTemplate.convertAndSend(
                    QueueEnum.QUEUE_TTL_ORDER_CANCEL.getExchange(),
                    QueueEnum.QUEUE_TTL_ORDER_CANCEL.getRouteKey(),
                    orderId,
                    (Message message) -> {
                        message.getMessageProperties().setDeliveryMode(MessageDeliveryMode.PERSISTENT);
                        message.getMessageProperties().setExpiration(String.valueOf(delayTimes));
                        return message;
                    },
                    correlationData
            );

            LOGGER.info("已发送订单延迟消息: orderId={}, messageId={}", orderId, messageId);
        } catch (Exception e) {
            LOGGER.error("消息发送异常: orderId={}", orderId, e);
            retryMessageDao.updateStatus(messageId, 2); // 2=发送失败
        }
    }

    /**
     * 生成唯一消息ID
     */
    private String generateMessageId(Long orderId) {
        return orderId + "_" + System.currentTimeMillis();
    }

    

//    public void sendMessage(Long orderId, long delayTimes){
//        amqpTemplate.convertAndSend(QueueEnum.QUEUE_TTL_ORDER_CANCEL.getExchange(), QueueEnum.QUEUE_TTL_ORDER_CANCEL.getRouteKey(), orderId, new MessagePostProcessor() {
//            @Override
//            public Message postProcessMessage(Message message) throws AmqpException {
//                message.getMessageProperties().setExpiration(String.valueOf(delayTimes));
//                return message;
//            }
//        });
//        
//        
//        LOGGER.info("send orderId:{}",orderId);
//    }
}
