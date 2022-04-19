package com.bjpowernode.p2p.service;

import com.bjpowernode.p2p.model.loan.LoanInfo;
import com.bjpowernode.p2p.model.vo.PaginationVO;

import java.util.HashMap;
import java.util.List;

public interface  LoanInfoService {

    /**
     * 查询平台历史平均年华收益率
     * @return
     */
    double loanInfoHistoryAvgRate();


    /**
     * 根据产品类型，获取产品列表
     * @param params
     * @return
     */
    List queryLoanInfoListProductType(HashMap params);


    /**
     * 分页查询产品列表
     * @param params
     * @return
     */
    PaginationVO queryLoanInfoListByPage(HashMap<String, Object> params);

    /**
     * 查询对应产品详细信息
     * @return
     */
    LoanInfo queryLoanInfoProductData(Integer id);

    /**
     * 修改产品可投余额
     * @param loanInfo
     * @return
     */
    Integer modifyleftProductMoney(LoanInfo loanInfo);

    /**
     * 立即投资
     * @param param
     */
    void invest(HashMap param) throws Exception;
}
