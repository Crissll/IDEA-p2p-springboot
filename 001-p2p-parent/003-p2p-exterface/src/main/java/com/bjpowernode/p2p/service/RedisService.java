package com.bjpowernode.p2p.service;

import java.util.concurrent.TimeUnit;

public interface RedisService {

    /**
     * 数据保存
     * @param phone
     * @param randomCode
     */
    void put(String phone, Object randomCode, Integer timeout, TimeUnit timeUnit);

    Object getRedisCode(String phone);
}
