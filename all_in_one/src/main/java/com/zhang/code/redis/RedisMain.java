package com.zhang.code.redis;

import com.zhang.code.redis.config.RedisConfig;
import com.zhang.code.redis.util.RedisOpsUtil;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.data.redis.core.RedisTemplate;

public class RedisMain {
    public static void main(String[] args) {
        ApplicationContext ctx = new AnnotationConfigApplicationContext(RedisConfig.class);
        RedisTemplate redisTemplate = ctx.getBean(RedisTemplate.class);

        // 直接操作 redisTemplate
        /*redisTemplate.opsForValue().set("key1", "value1");
        redisTemplate.opsForHash().put("hash", "field", "hvalue");
        System.out.println(redisTemplate.opsForValue().get("key1"));
        System.out.println(redisTemplate.opsForHash().get("hash", "field"));*/

        // 执行多个
        RedisOpsUtil.useSessionCallback(redisTemplate);
    }
}
