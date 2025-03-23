package com.pwc.portal.domain;

import lombok.Getter;

@Getter
public enum QueueEnum {
    QUEUE_ORDER_CANCEL("order.direct", "order.cancel", "order.cancel"),

    QUEUE_TTL_ORDER_CANCEL("order.direct.ttl", "order.cancel.ttl", "order.cancel.ttl");
    
    private final String exchange;
    private final String name;
    private final String routeKey;

    QueueEnum(String exchange, String name, String routeKey) {
        this.exchange = exchange;
        this.name = name;
        this.routeKey = routeKey;
    }
}