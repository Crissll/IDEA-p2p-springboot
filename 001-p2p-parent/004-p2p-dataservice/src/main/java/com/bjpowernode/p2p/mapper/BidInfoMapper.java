package com.bjpowernode.p2p.mapper;

import com.bjpowernode.p2p.model.loan.BidInfo;

import java.util.List;
import java.util.Map;

public interface BidInfoMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(BidInfo record);

    int insertSelective(BidInfo record);

    BidInfo selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(BidInfo record);

    int updateByPrimaryKey(BidInfo record);

    Double selectAllBidMoney();

    List<BidInfo> selectRecentlyByLoanId(Map params);

    List<BidInfo> selectAllBidRecord(Map params);

    /**
     * 根据产品id查询用户投资信息
     * @param id
     * @return
     */
    List<BidInfo> selectBidInfoByLoanId(Integer id);
}