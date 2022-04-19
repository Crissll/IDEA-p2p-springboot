package com.bjpowernode.p2p.service;

import com.bjpowernode.p2p.model.loan.BidInfo;
import com.bjpowernode.p2p.model.vo.BidUserVOList;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public interface BidInfoService {
    /**
     * 查询产品投资总额
     * @return
     */
    Double queryAllBidMoney();

    /**
     * 查询产品投产记录
     * @param params
     * @return
     */
    List<BidInfo> queryRecentlyByLoanId(Map params);

    /**
     * 根据用户id查询投资记录
     * @param params
     * @return
     */
    List<BidInfo> queryAllBidRecord(Map params);

    /**
     * 获取投资排行榜
     * @return
     */
    List<BidUserVOList> queryBidUserTop();
}
