package com.zhang.code.redis.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.data.redis.core.*;
import org.springframework.data.redis.core.script.DefaultRedisScript;
import org.springframework.data.redis.serializer.RedisSerializer;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import redis.clients.jedis.Jedis;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("/redis")
public class RedisController {
    @Autowired
    private RedisTemplate redisTemplate;

    @Autowired
    private StringRedisTemplate stringRedisTemplate;

    @RequestMapping("/stringAndHash")
    @ResponseBody
    public Map<String, Object> testStringAndHash() {
        redisTemplate.opsForValue().set("key1", "value1");
        // 注意这里使用了JDK的序列化器，所以Redis保存时不是整数，不能运算
        redisTemplate.opsForValue().set("int_key", "1");
        stringRedisTemplate.opsForValue().set("int", "1");
        // 使用运算
        stringRedisTemplate.opsForValue().increment("int", 1);
        // 获取底层Jedis连接
        Jedis jedis = (Jedis) stringRedisTemplate.getConnectionFactory().getConnection().getNativeConnection();
        // 减1操作，这个命令RedisTemplate不支持，所以先获取底层的连接再操作
        jedis.decr("int");
        Map<String, Object> hash = new HashMap<>();
        hash.put("field1", "value1");
        hash.put("field2", "value2");
        // 存入一个散列数据类型
        stringRedisTemplate.opsForHash().putAll("hash", hash);
        // 新增一个字段
        stringRedisTemplate.opsForHash().put("hash", "field3", "value3");
        // 绑定散列操作的key, 这样可以连续对同一个散列数据类型进行操作
        BoundHashOperations<String, Object, Object> hashOps = stringRedisTemplate.boundHashOps("hash");
        // 删除两个字段
        hashOps.delete("field1", "field2");
        // 新增一个字段
        hashOps.put("field4", "value5");
        Map<String, Object> map = new HashMap<>();
        map.put("success", true);
        return map;
    }

    @RequestMapping("/pipeline")
    @ResponseBody
    public Map<String, Object> testPipeline() {
        long start = System.currentTimeMillis();
        List list = (List) redisTemplate.executePipelined(new SessionCallback<Object>() {
            @Override
            public <K, V> Object execute(RedisOperations<K, V> redisOperations) throws DataAccessException {
                for (int i = 0; i < 10; i++) {
//                    redisOperations.opsForValue().set("pipeline_" + i, "value_" + i);
                }
                return null;
            }
        });
        Map<String, Object> map = new HashMap<>();
        map.put("success", true);
        return map;
    }

    @RequestMapping("/message/{topic}/{msg}")
    @ResponseBody
    public Map<String, Object> testMessage(@PathVariable String topic, @PathVariable String msg) {
        redisTemplate.convertAndSend(topic, msg);
        Map<String, Object> map = new HashMap<>();
        map.put("success", true);
        return map;
    }

    @RequestMapping("/lua")
    @ResponseBody
    public Map<String, Object> testLua() {
        DefaultRedisScript<String> rs = new DefaultRedisScript<>();
        // 设置脚本
        rs.setScriptText("return 'Hello Redis'");
        // 定义返回类型。注意：如果没有这个定义，Spring不会返回结果
        rs.setResultType(String.class);
        RedisSerializer stringSerializer = redisTemplate.getStringSerializer();
        // 执行 lua 脚本
        String str = (String) redisTemplate.execute(rs, stringSerializer, stringSerializer, null);
        Map<String, Object> map = new HashMap<>();
        map.put("success", true);
        map.put("str", str);
        return map;
    }

    @RequestMapping("/lua2")
    @ResponseBody
    public Map<String, Object> testLua2(String key1, String key2, String value1, String value2) {
        // 定义 lua 脚本
        String lua = "redis.call('set', KEYS[1], ARGV[1]) \n" +
                "redis.call('set', KEYS[2], ARGV[2]) \n" +
                "local str1 = redis.call('get', KEYS[1]) \n" +
                "local str2 = redis.call('get', KEYS[2]) \n" +
                "if str1 == str2 then \n" +
                "return 1 \n" +
                "end \n" +
                "return 0 \n";
        System.out.println(lua);
        // 结果返回为Long
        DefaultRedisScript<Long> rs = new DefaultRedisScript<>();
        rs.setScriptText(lua);
        rs.setResultType(Long.class);
        // 采用字符串序列化器
        RedisSerializer stringSerializer = redisTemplate.getStringSerializer();
        // 定义 key 参数
        List<String> keyList = new ArrayList<>();
        keyList.add(key1);
        keyList.add(key2);
        // 传递两个参数值，其中第一个序列化器是 key 的序列化器，第二个序列化器是参数的序列化器
        Long result = (Long) redisTemplate.execute(rs, stringSerializer, stringSerializer, keyList, value1, value2);
        Map<String, Object> map = new HashMap<>();
        map.put("success", true);
        map.put("result", result);
        return map;
    }
}
