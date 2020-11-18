package com.zhang.code.redis.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.connection.RedisStandaloneConfiguration;
import org.springframework.data.redis.connection.jedis.JedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.RedisSerializer;
import redis.clients.jedis.JedisPoolConfig;

/*@Configuration // 测试打开*/
public class RedisConfig {
    private RedisConnectionFactory redisConnectionFactory;

    @Bean(name = "redisConnectionFactory")
    public RedisConnectionFactory initRedisConnectionFactory() {
        if (this.redisConnectionFactory != null) {
            return this.redisConnectionFactory;
        }
        JedisPoolConfig poolConfig = new JedisPoolConfig();
        // 最大空闲数
        poolConfig.setMaxIdle(30);
        // 最大连接数
        poolConfig.setMaxTotal(50);
        // 最大等待毫秒数
        poolConfig.setMaxWaitMillis(2000);
        // 创建Jedis连接工厂
        JedisConnectionFactory connectionFactory = new JedisConnectionFactory(poolConfig);
        // 获取单机的Redis配置
        RedisStandaloneConfiguration rsCfg = connectionFactory.getStandaloneConfiguration();
        connectionFactory.setHostName("127.0.0.1");
        connectionFactory.setPort(6379);
//        connectionFactory.setPassword("123456");
        this.redisConnectionFactory = connectionFactory;
        return connectionFactory;
    }

    @Bean(name = "redisTemplate")
    public RedisTemplate<Object, Object> initRedisTemplate() {
        RedisTemplate<Object, Object> redisTemplate = new RedisTemplate<>();
        // RedisTemplate 会自动初始化 StringRedisSerializer, 所以这里直接获取
        RedisSerializer<String> stringRedisSerializer = redisTemplate.getStringSerializer();
        // 设置字符串序列化器，这样Spring就会把Redis的key当作字符串处理了
        redisTemplate.setKeySerializer(stringRedisSerializer);
        redisTemplate.setHashKeySerializer(stringRedisSerializer);
        redisTemplate.setHashValueSerializer(stringRedisSerializer);
        redisTemplate.setConnectionFactory(initRedisConnectionFactory());
        return redisTemplate;
    }
}