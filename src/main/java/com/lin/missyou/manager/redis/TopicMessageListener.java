package com.lin.missyou.manager.redis;

import com.lin.missyou.model.businessObject.OrderMessageBo;
import com.lin.missyou.service.CouponBackService;
import com.lin.missyou.service.OrderCancelService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.data.redis.connection.Message;
import org.springframework.data.redis.connection.MessageListener;


public class TopicMessageListener implements MessageListener {

    private static ApplicationEventPublisher publisher;

    @Autowired //IOC容器里注入的
    public void setPublisher(ApplicationEventPublisher publisher){
        TopicMessageListener.publisher = publisher;
    }

    @Override
    //接受redis键空间通知
    public void onMessage(Message message, byte[] bytes) {
        byte[] body = message.getBody();
        byte[] channel = message.getChannel();

        String expiredKey = new String(body);
        String topic = new String(channel);

        OrderMessageBo orderMessageBo = new  OrderMessageBo(expiredKey);
        TopicMessageListener.publisher.publishEvent(orderMessageBo);
    }
}
