package com.lin.missyou.core.enumeration;

import java.util.stream.Stream;

public enum CouponType {
    FULL_MINUS(1,"满减券"),
    FULL_OFF(2,"满折券"),
    NO_THRESHOLD_MINUS(3,"无门槛减除券");

    private Integer value;

    private CouponType(Integer value, String description){
        this.value = value;
    }

    public static CouponType toValue(int value){
        return Stream.of(CouponType.values())
                .filter(c->c.value==value)
                .findAny().orElse(null);
    }

    public int value(){
        return this.value;
    }
}
