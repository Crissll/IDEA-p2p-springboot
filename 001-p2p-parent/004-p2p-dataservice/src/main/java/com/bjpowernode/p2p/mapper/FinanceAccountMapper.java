package com.bjpowernode.p2p.mapper;

import com.bjpowernode.p2p.model.user.FinanceAccount;

import java.util.HashMap;
import java.util.Map;

public interface FinanceAccountMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(FinanceAccount record);

    int insertSelective(FinanceAccount record);

    FinanceAccount selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(FinanceAccount record);

    int updateByPrimaryKey(FinanceAccount record);

    /**
     *根据用户id 查询账户信息
     * @param id
     * @return
     */
    FinanceAccount selectByPrimaryUid(Integer id);

    /**
     * 更新账户余额
     * @param param
     * @return
     */
    int updateByInvest(HashMap param);

    /**
     * 返还收益
     * @param params
     * @return
     */
    int updateByIncomeMoney(Map params);

    /**
     * 根据充值金额修改对应用户的余额
     * @param rechargeMoney
     * @param uid
     * @return
     */
    int updateByprimaryPayMoney(Double rechargeMoney, Integer uid);
}