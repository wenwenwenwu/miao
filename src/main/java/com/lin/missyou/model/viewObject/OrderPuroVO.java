package com.lin.missyou.model.viewObject;

import com.github.dozermapper.core.DozerBeanMapperBuilder;
import com.github.dozermapper.core.Mapper;
import com.lin.missyou.model.dataAccessObject.Order;

import java.util.Date;

public class OrderPuroVO extends Order {

    private Long period;

    public OrderPuroVO(Order order, Long period){
        Mapper mapper = DozerBeanMapperBuilder.buildDefault();
        mapper.map(order,this); //this表示类
        this.period = period;
    }
}
