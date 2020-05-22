package com.lin.missyou.core.enumeration;

import java.util.stream.Stream;

public enum OrderStatus {
    ALL(0,"全部"),
    UNPAID(1,"待支付"),
    PAID(2,"已支付"),
    DELIVERED(3,"已发货"),
    FINISHED(4,"已完成"),
    CANCELED(5,"已取消");

    private Integer value;

    private OrderStatus(Integer value, String description){
        this.value = value;
    }

    public static OrderStatus toValue(int value){
        return Stream.of(OrderStatus.values())
                .filter(c->c.value==value)
                .findAny().orElse(null);
    }

    public int value(){
        return this.value;
    }
}
