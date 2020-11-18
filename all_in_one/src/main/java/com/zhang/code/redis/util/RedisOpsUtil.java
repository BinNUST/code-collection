package com.zhang.code.redis.util;

import org.springframework.dao.DataAccessException;
import org.springframework.data.redis.connection.RedisConnection;
import org.springframework.data.redis.core.RedisCallback;
import org.springframework.data.redis.core.RedisOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.SessionCallback;

public class RedisOpsUtil {
    /**
     * 需要处理底层的转换规则，如果不考虑改写底层，尽量不使用它
     * @param redisTemplate
     */
    public static void useRedisCallback(RedisTemplate redisTemplate) {
        redisTemplate.execute(new RedisCallback() {
            @Override
            public Object doInRedis(RedisConnection redisConnection) throws DataAccessException {
                redisConnection.set("key1".getBytes(), "value1".getBytes());
                redisConnection.hSet("hash".getBytes(), "field".getBytes(), "hvalue".getBytes());
                return null;
            }
        });
    }

    /**
     * 高级接口，比较友好，一般情况下，优先使用
     * @param redisTemplate
     */
    public static void useSessionCallback(RedisTemplate redisTemplate) {
        redisTemplate.execute(new SessionCallback() {
            @Override
            public Object execute(RedisOperations redisOperations) throws DataAccessException {
                redisOperations.opsForValue().set("key1", "value1");
                redisOperations.opsForHash().put("hash", "field", "hvalue");
                return null;
            }
        });
    }
}
