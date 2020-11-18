package com.zhang.code.redis.component;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.data.redis.connection.MessageListener;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.listener.ChannelTopic;
import org.springframework.data.redis.listener.RedisMessageListenerContainer;
import org.springframework.data.redis.listener.Topic;
import org.springframework.data.redis.serializer.RedisSerializer;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;

@Component
public class MainTest {
    // 注入 RedisTemplate
    @Autowired
    private RedisTemplate redisTemplate;

    // Redis连接工厂
    @Autowired
    private RedisConnectionFactory factory;

    // Redis 消息监听器
    @Autowired
    private MessageListener redisMsgListener;

    // 任务池
    private ThreadPoolTaskExecutor taskExecutor;

    /**
     * 创建任务池，运行线程等待处理redis的消息
     * @return
     */
    @Bean
    public ThreadPoolTaskExecutor initTaskScheduler() {
        if (taskExecutor != null) {
            return taskExecutor;
        }
        taskExecutor = new ThreadPoolTaskExecutor();
        taskExecutor.setCorePoolSize(20);
        return taskExecutor;
    }

    @Bean
    public RedisMessageListenerContainer initRedisContainer() {
        RedisMessageListenerContainer container = new RedisMessageListenerContainer();
        // redis 连接工厂
        container.setConnectionFactory(factory);
        // 设置运行任务池
        container.setTaskExecutor(initTaskScheduler());
        // 定义监听渠道，名称为 topic1
        Topic topic = new ChannelTopic("topic1");
        container.addMessageListener(redisMsgListener, topic);
        return container;
    }

    // 定义自定义后初始化方法
    @PostConstruct
    public void init() {
        initRedisTemplate();
    }

    // 设置 RedisTemplate 的序列化器
    private void initRedisTemplate() {
        RedisSerializer stringSerializer = redisTemplate.getStringSerializer();
        redisTemplate.setKeySerializer(stringSerializer);
        redisTemplate.setHashKeySerializer(stringSerializer);
    }
}
