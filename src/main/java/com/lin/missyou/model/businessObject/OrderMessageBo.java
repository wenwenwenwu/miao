package com.lin.missyou.model.businessObject;

import lombok.Getter;
import lombok.Setter;

import java.lang.reflect.Array;
import java.util.List;

@Setter
@Getter
public class OrderMessageBo {

    private Long orderId;
    private Long couponId;
    private Long userId;
    private String message;

    public OrderMessageBo(String message){
        this.message = message;
    }

    private void parseId(String message){
        String[] temp = message.split(",");
        this.userId = Long.valueOf(temp[0]);
        this.orderId = Long.valueOf(temp[1]);
        this.couponId = Long.valueOf(temp[2]);
    }
}
