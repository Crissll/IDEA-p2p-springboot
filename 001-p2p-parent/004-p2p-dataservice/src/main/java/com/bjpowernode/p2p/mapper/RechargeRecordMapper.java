package com.bjpowernode.p2p.mapper;

import com.bjpowernode.p2p.model.loan.RechargeRecord;

import java.util.Map;

public interface RechargeRecordMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(RechargeRecord record);

    int insertSelective(RechargeRecord record);

    RechargeRecord selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(RechargeRecord record);

    int updateByPrimaryKey(RechargeRecord record);

    RechargeRecord selectRechargeRecordByUid(Map params);

    /**
     * 根据订单号查询订单记录
     * @param orderId
     * @return
     */
    RechargeRecord selectByPrimaryNo(String orderId);
}