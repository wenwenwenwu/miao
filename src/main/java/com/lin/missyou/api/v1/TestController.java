package com.lin.missyou.api.v1;

import com.lin.missyou.manager.rocketmq.ProducerSchedule;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequestMapping("/test")
@RestController
public class TestController {

    @Autowired
    private ProducerSchedule producerSchedule;

    @GetMapping("/push")
    public void pushMessageToMQ() throws Exception {
        producerSchedule.send("TopicTest","你是不是一只狗啊");
    }
}
