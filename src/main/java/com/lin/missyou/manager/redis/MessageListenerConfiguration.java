package com.lin.missyou.manager.redis;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.listener.PatternTopic;
import org.springframework.data.redis.listener.RedisMessageListenerContainer;
import org.springframework.data.redis.listener.Topic;

//@Configuration注解标识了一个配置类，配置类的@Bean方法中返回的对象会作为单例bean被添加到IOC容器中。
@Configuration
public class MessageListenerConfiguration {

    @Value("${spring.redis.listen-pattern}")
    public String pattern;

    @Bean
    public RedisMessageListenerContainer listenerContainer(RedisConnectionFactory redisConnection){
        //RedisMessageListenerContainer负责SpringBoot中的代码和Redis的联系
        RedisMessageListenerContainer container = new RedisMessageListenerContainer();
        //建立连接
        container.setConnectionFactory(redisConnection);
        //创建监听主题
        Topic topic = new PatternTopic(this.pattern);
        //添加监听器和监听的主题
        container.addMessageListener(new TopicMessageListener(),topic);
        return container;
    }
}
