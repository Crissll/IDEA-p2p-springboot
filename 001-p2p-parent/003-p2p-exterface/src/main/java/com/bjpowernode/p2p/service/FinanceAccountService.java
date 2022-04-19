package com.bjpowernode.p2p.service;

import com.bjpowernode.p2p.model.user.FinanceAccount;

public interface FinanceAccountService {
    /**
     * 根据用户id查询账户余额
     * @param id
     * @return
     */
    FinanceAccount queryFianceAccountByUid(Integer id);

    /**
     * 修改用户余额
     * @param financeAccount
     * @return
     */
    Integer modifyAvailableMoney(FinanceAccount financeAccount);
}
