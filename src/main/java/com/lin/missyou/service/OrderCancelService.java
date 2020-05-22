package com.lin.missyou.service;

import com.lin.missyou.exception.exception.ServerErrorException;
import com.lin.missyou.model.businessObject.OrderMessageBo;
import com.lin.missyou.model.dataAccessObject.Order;
import com.lin.missyou.repository.OrderRepository;
import com.lin.missyou.repository.SkuRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
public class OrderCancelService {

    @Autowired
    private OrderRepository orderRepository;

    @Autowired
    private SkuRepository skuRepository;

    @EventListener
    @Transactional
    public void cancel(OrderMessageBo orderMessageBo) {
        if (orderMessageBo.getOrderId() <= 0) {
            throw new ServerErrorException(9999);
        }
        this.cancel(orderMessageBo.getOrderId());
    }

    private void cancel(Long oid) {
        Optional<Order> orderOptional = orderRepository.findById(oid);
        Order order = orderOptional.orElseThrow(() -> new ServerErrorException(9999));
        //更改订单状态
        int res = orderRepository.cancelOrder(order.getId());
        if (res != 1) {
            return;
        }
        //归还库存
        order.getSnapItems().forEach(item ->
                this.skuRepository.recoverStock(item.getId(), item.getCount().longValue())
        );
    }
}
