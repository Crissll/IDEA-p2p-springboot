package com.bjpowernode.p2p.service.impl;
import com.alibaba.dubbo.config.annotation.Service;
import com.bjpowernode.p2p.service.RedisService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.StringRedisSerializer;
import org.springframework.stereotype.Component;

import java.util.concurrent.TimeUnit;

@Component
@Service(interfaceClass = RedisService.class,version = "1.0.0",timeout = 15000)
public class RedisServiceImpl implements RedisService {

    @Autowired
    private RedisTemplate redisTemplate;

    @Override
    public void put(String key, Object value,Integer timeout,TimeUnit timeUnit) {
        redisTemplate.setKeySerializer(new StringRedisSerializer());
        redisTemplate.opsForValue().set(key, value,timeout, timeUnit);
    }

    @Override
    public Object getRedisCode(String phone) {
        return redisTemplate.opsForValue().get(phone);
    }
}
