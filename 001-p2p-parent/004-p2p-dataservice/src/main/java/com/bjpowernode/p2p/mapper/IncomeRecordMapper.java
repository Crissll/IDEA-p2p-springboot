package com.bjpowernode.p2p.mapper;

import com.bjpowernode.p2p.model.loan.IncomeRecord;

import java.util.List;

public interface IncomeRecordMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(IncomeRecord record);

    int insertSelective(IncomeRecord record);

    IncomeRecord selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(IncomeRecord record);

    int updateByPrimaryKey(IncomeRecord record);

    /**
     * 查询投资状态未返还的记录
     * @param status
     * @return
     */
    List<IncomeRecord> selectByIncomeStatus(int status);
}