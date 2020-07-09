package com.lin.missyou.manager.rocketmq;

import org.apache.rocketmq.client.exception.MQClientException;
import org.apache.rocketmq.client.producer.DefaultMQProducer;
import org.apache.rocketmq.client.producer.SendResult;
import org.apache.rocketmq.common.message.Message;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;

@Component
public class ProducerSchedule {

    private DefaultMQProducer producer;

    @Value("${spring.rocketmq.producer.producer-group}")
    private String producerGroup;

    @Value("${spring.rocketmq.namesrv-addr}")
    private String namesrvAddr;

    //执行顺序：构造完成->注入bean、配置值
    //如果对象构造需要使用被注入内容的话，这个步骤要在@PostConstruct方法而非构造器中
    @PostConstruct
    public void defaultMQProducer(){
        if (this.producer == null){
            this.producer = new DefaultMQProducer(this.producerGroup);
            this.producer.setNamesrvAddr(this.namesrvAddr);
        }
        try {
            this.producer.start();
            System.out.println("-------producer start");
        } catch (MQClientException e){
            e.printStackTrace();
        }
    }

    public String send(String topic, String messageText) throws Exception{
        Message message = new Message(topic,messageText.getBytes());
        //messageDelayLevel = 1s 5s 10s 30s 1m 2m 3m 4m 5m 6m 7m 8m 9m 10m 20m 30m 1h 2h
        message.setDelayTimeLevel(4);//30s
        SendResult result = this.producer.send(message);
        System.out.println(result.getMsgId());
        System.out.println(result.getSendStatus());
        return result.getMsgId();
    }
}
