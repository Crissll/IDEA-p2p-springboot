package com.bjpowernode.p2p.mapper;

import com.bjpowernode.p2p.model.loan.LoanInfo;

import java.util.HashMap;
import java.util.List;

public interface LoanInfoMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(LoanInfo record);

    int insertSelective(LoanInfo record);

    LoanInfo selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(LoanInfo record);

    int updateByPrimaryKey(LoanInfo record);


    /**
     * 查询平台历史平均年化收益率
     * @return
     */
    double selectHistoryAvgRate();

    /**
     * 根据产品类型，获取产品列表
     * @param params
     * @return
     */
    List selectLoanInfoListProductType(HashMap params);

    /**
     * 分页查询产品列表
     * @param params
     * @return
     */
    Integer selectLoanSize(HashMap<String, Object> params);

    /**
     *根据id查询产品信息
     * @param id
     * @return
     */
    LoanInfo selectLoanInfoProductData(Integer id);

    /**
     * 投资更新产品剩余可投金额
     * @param param
     * @return
     */
    int updateLeftProductMoneyByInvest(HashMap param);

    /**
     * 查询满标产品
     * @param i
     * @return
     */
    List<LoanInfo> selectLoanInfoByProductStatus(int productStatus);
}