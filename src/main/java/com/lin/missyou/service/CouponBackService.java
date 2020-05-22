package com.lin.missyou.service;

import com.lin.missyou.core.LocalUser;
import com.lin.missyou.core.enumeration.OrderStatus;
import com.lin.missyou.exception.exception.ServerErrorException;
import com.lin.missyou.model.businessObject.OrderMessageBo;
import com.lin.missyou.model.dataAccessObject.Order;
import com.lin.missyou.repository.OrderRepository;
import com.lin.missyou.repository.UserCouponRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
public class CouponBackService {

    @Autowired
    private OrderRepository orderRepository;

    @Autowired
    private UserCouponRepository userCouponRepository;

    @EventListener
    @Transactional
    public void returnCoupon(OrderMessageBo orderMessageBo){
        Long couponId = orderMessageBo.getCouponId();
        Long uid = orderMessageBo.getUserId();
        Long oid = orderMessageBo.getOrderId();

        if(couponId == -1){
            return;
        }

        Optional<Order> orderOptional = this.orderRepository.findFirstByUserIdAndId(uid,oid);
        Order order = orderOptional.orElseThrow(()-> new ServerErrorException(9999));
        if (order.getStatusEnum().equals(OrderStatus.UNPAID) || order.getStatusEnum().equals(OrderStatus.CANCELED)){
                this.userCouponRepository.returnBack(couponId,oid);
        }
    }
}
