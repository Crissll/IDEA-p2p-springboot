package com.bjpowernode.p2p.service;

import java.util.Map;

public interface KuaiQianService {
    /**
     * 支付
     * @param rechargeMoney
     * @param name
     * @param phone
     * @return
     */
    Map makeKuaiQianRequestParam(String rechargeMoney, String name, String phone,String OrderId);
}
