package com.pwc.portal.domain;

import lombok.Builder;
import lombok.Data;
import java.util.Date;

@Data
public class RetryMessage {
    /**
     * 主键ID
     */
    private Long id;

    /**
     * 关联订单ID（对应 oms_order.id）
     */
    private Long orderId;

    /**
     * 消息唯一ID（格式：orderId_timestamp 或 UUID）
     */
    private String messageId;

    /**
     * 重试次数（默认0，最大3次）
     */
    private Integer retryCount;

    /**
     * 消息状态：
     * 0=待确认（已发送未收到Broker确认）
     * 1=已确认（Broker确认成功）
     * 2=已失败（超过最大重试次数）
     */
    private Integer status;

    /**
     * 延迟时间（毫秒，用于重试时恢复原始TTL）
     */
    private Long delayTimes;

    /**
     * 创建时间
     */
    private Date createTime;
}