package com.lin.missyou.service;

import com.github.wxpay.sdk.WXPay;
import com.github.wxpay.sdk.WXPayUtil;
import com.lin.missyou.core.enumeration.OrderStatus;
import com.lin.missyou.exception.exception.ServerErrorException;
import com.lin.missyou.model.dataAccessObject.Order;
import com.lin.missyou.repository.OrderRepository;
import org.hibernate.SessionException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@Service
public class WxPaymentNotifyService {

    @Autowired
    private OrderRepository orderRepository;

    @Autowired
    private WxPaymentService wxPaymentService;

    @Transactional
    public void processPayNotify(String xml) {
        //xml格式字符串转Map对象
        Map<String, String> dataMap = new HashMap<>();
        try {
            dataMap = WXPayUtil.xmlToMap(xml);
        } catch (Exception e) {
            e.printStackTrace();
        }
        //创建wxPay对象
        WXPay wxPay = this.wxPaymentService.assmbleWxPayConfig();
        //校验支付结果中的数字签名
        boolean volid;
        try {
            volid = wxPay.isPayResultNotifySignatureValid(dataMap);
        } catch (Exception e) {
            e.printStackTrace();
            throw new ServerErrorException(9999);
        }
        //校验成功，但是签名无效
        if (!volid) {
            throw new ServerErrorException(9999);
        }
        //校验支付结果中的数据
        String returnCode = dataMap.get("return_code");
        String orderNo = dataMap.get("out_trade_no");
        String resultCode = dataMap.get("result_code");
        if (!returnCode.equals("SUCCESS")) {
            throw new ServerErrorException(9999);
        }
        if (!resultCode.equals("SUCCESS")){
            throw  new ServerErrorException(9999);
        }
        if (orderNo == null) {
            throw new ServerErrorException(9999);
        }
        //更新订单状态
        this.deal(orderNo);
    }

    private void deal(String orderNo) {
        Optional<Order> orderOptional = orderRepository.findFirstByOrderNo(orderNo);
        Order order = orderOptional.orElseThrow(() -> new ServerErrorException(9999));
        int res = -1;
        //防止延迟消息队列在用户支付成功、发送给服务端的时间内把订单状态改为已取消
        if (order.getStatus().equals(OrderStatus.UNPAID.value()) ||
                order.getStatus().equals(OrderStatus.CANCELED.value())) {
           res = this.orderRepository.updateStatusByOrderNo(orderNo, OrderStatus.PAID.value());
        }
        if (res != 1){
            throw new ServerErrorException(9999);
        }
    }
}
