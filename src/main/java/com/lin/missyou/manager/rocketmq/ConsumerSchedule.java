package com.lin.missyou.manager.rocketmq;

import org.apache.rocketmq.client.consumer.DefaultMQPushConsumer;
import org.apache.rocketmq.client.consumer.listener.ConsumeConcurrentlyContext;
import org.apache.rocketmq.client.consumer.listener.ConsumeConcurrentlyStatus;
import org.apache.rocketmq.client.consumer.listener.MessageListenerConcurrently;
import org.apache.rocketmq.client.exception.MQClientException;
import org.apache.rocketmq.common.message.Message;
import org.apache.rocketmq.common.message.MessageExt;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;
import sun.tools.jar.CommandLine;

import java.util.List;

@Component
//CommandLineRunner接口的run方法。应用程序启动时初始化一些资源，
public class ConsumerSchedule implements CommandLineRunner {

    @Value("${spring.rocketmq.consumer.consumer-group}")
    private String consumerGroup;

    @Value("${spring.rocketmq.namesrv-addr}")
    private String namesrvAddr;

    @Override
    public void run(String... args) throws Exception {
        this.messageListener();
    }

    public void messageListener() throws MQClientException {
        DefaultMQPushConsumer consumer = new DefaultMQPushConsumer(this.consumerGroup);
        consumer.setNamesrvAddr(this.namesrvAddr);
        consumer.subscribe("TopicTest","*");
        consumer.setConsumeMessageBatchMaxSize(1);
        //registerMessageListener方法接收一个实现MessageListenerConcurrently接口的匿名类对象
        consumer.registerMessageListener(new MessageListenerConcurrently() {
            //会调用该方法
            @Override
            public ConsumeConcurrentlyStatus consumeMessage(List<MessageExt> messageExts, ConsumeConcurrentlyContext consumeConcurrentlyContext) {
                for (MessageExt message: messageExts){
                    System.out.println("消息："+ new String(message.getBody()));
                }
                return ConsumeConcurrentlyStatus.CONSUME_SUCCESS;
            }
        });
        consumer.start();
    }

}
