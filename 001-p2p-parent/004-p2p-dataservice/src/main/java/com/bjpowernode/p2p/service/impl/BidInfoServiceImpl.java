package com.bjpowernode.p2p.service.impl;

import com.alibaba.dubbo.config.annotation.Service;
import com.bjpowernode.p2p.cons.Constants;
import com.bjpowernode.p2p.mapper.BidInfoMapper;
import com.bjpowernode.p2p.model.loan.BidInfo;
import com.bjpowernode.p2p.model.vo.BidUserVOList;
import com.bjpowernode.p2p.service.BidInfoService;
import org.apache.commons.lang3.ObjectUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ZSetOperations;
import org.springframework.stereotype.Component;

import java.util.*;
import java.util.concurrent.TimeUnit;

@Component
@Service(interfaceClass = BidInfoService.class,version = "1.0.0",timeout = 15000)
public class BidInfoServiceImpl implements BidInfoService {

    @Autowired
    private BidInfoMapper bidInfoMapper;

    @Autowired
    private RedisTemplate redisTemplate;

    @Override
    public Double queryAllBidMoney() {
        Double allBidMoney = (Double) redisTemplate.opsForValue().get(Constants.ALL_BID_MONEY);

        if (!ObjectUtils.allNotNull(allBidMoney)){
            synchronized (this){
                allBidMoney = (Double) redisTemplate.opsForValue().get(Constants.ALL_BID_MONEY);
                if (!ObjectUtils.allNotNull(allBidMoney)){
                    allBidMoney = bidInfoMapper.selectAllBidMoney();
                    redisTemplate.opsForValue().set(Constants.ALL_BID_MONEY, allBidMoney,1, TimeUnit.DAYS);
                }
            }
        }

        return allBidMoney;
    }

    @Override
    public List<BidInfo> queryRecentlyByLoanId(Map params) {
        return bidInfoMapper.selectRecentlyByLoanId(params);
    }

    @Override
    public List<BidInfo> queryAllBidRecord(Map params) {
        return bidInfoMapper.selectAllBidRecord(params);
    }

    @Override
    public List<BidUserVOList> queryBidUserTop() {
        List<BidUserVOList> bidUserVOList = new ArrayList();
        Set<ZSetOperations.TypedTuple<Object>> set = redisTemplate.opsForZSet().reverseRangeWithScores(Constants.INVEST_TOP, 0, 5);
        Iterator<ZSetOperations.TypedTuple<Object>> iterator = set.iterator();
        while (iterator.hasNext()){
            BidUserVOList bidUserVO = new BidUserVOList();
            ZSetOperations.TypedTuple<Object> next = iterator.next();
            Double score = next.getScore();
            String phone = (String) next.getValue();
            bidUserVO.setBidMoney(score);
            bidUserVO.setPhone(phone);
            bidUserVOList.add(bidUserVO);
        }
        return bidUserVOList;
    }


}
