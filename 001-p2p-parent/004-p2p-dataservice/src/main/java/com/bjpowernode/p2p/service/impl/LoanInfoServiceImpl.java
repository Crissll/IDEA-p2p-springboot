package com.bjpowernode.p2p.service.impl;
import com.alibaba.dubbo.config.annotation.Service;
import com.bjpowernode.p2p.cons.Constants;
import com.bjpowernode.p2p.mapper.BidInfoMapper;
import com.bjpowernode.p2p.mapper.FinanceAccountMapper;
import com.bjpowernode.p2p.mapper.LoanInfoMapper;
import com.bjpowernode.p2p.model.loan.BidInfo;
import com.bjpowernode.p2p.model.loan.LoanInfo;
import com.bjpowernode.p2p.model.vo.PaginationVO;
import com.bjpowernode.p2p.service.LoanInfoService;
import com.bjpowernode.p2p.service.RedisService;
import org.apache.commons.lang3.ObjectUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.concurrent.TimeUnit;

@Component
@Service(interfaceClass = LoanInfoService.class,version = "1.0.0",timeout = 15000)
public class LoanInfoServiceImpl implements LoanInfoService {

    @Autowired
    private  LoanInfoMapper loanInfoMapper;

    @Autowired
    private RedisService redisService;

    @Autowired
    private BidInfoMapper bidInfoMapper;

    @Autowired
    private FinanceAccountMapper financeAccountMapper;

    @Autowired
    private RedisTemplate redisTemplate;

    /**
     * 查询平均年化收益
     * @return
     */
    @Override
    //public synchronized double loanInfoHistoryAvgRate() {
    public synchronized double loanInfoHistoryAvgRate() {

        Double historyAvgRate = (Double) redisService.getRedisCode(Constants.HISTORY_AVG_PATE);
        if (!ObjectUtils.allNotNull(historyAvgRate)){
            synchronized (this){
                historyAvgRate = (Double) redisService.getRedisCode(Constants.HISTORY_AVG_PATE);
                if (!ObjectUtils.allNotNull(historyAvgRate)){
                    historyAvgRate = loanInfoMapper.selectHistoryAvgRate();
                    redisService.put(Constants.HISTORY_AVG_PATE, historyAvgRate,1, TimeUnit.DAYS);
                }
            }

        }
        return historyAvgRate;
    }

    /**
     * 根据产品类型，获取产品列表
     * @param params
     * @return
     */
    @Override
    public List queryLoanInfoListProductType(HashMap params) {
        return loanInfoMapper.selectLoanInfoListProductType(params);
    }

    /**
     * 根据产品类型，获取产品列表并分页查询
     * @param params
     * @return
     */
    @Override
    public PaginationVO queryLoanInfoListByPage(HashMap<String, Object> params) {

        List list = loanInfoMapper.selectLoanInfoListProductType(params);

        Integer loanSize = loanInfoMapper.selectLoanSize(params);

        PaginationVO paginationVO = new PaginationVO();
        paginationVO.setDatas(list);
        paginationVO.setTotalSize(loanSize);
        return paginationVO;
    }

    /**
     * 根据产品id查询产品信息
     * @param id
     * @return
     */
    @Override
    public LoanInfo queryLoanInfoProductData(Integer id) {
        return loanInfoMapper.selectByPrimaryKey(id);
    }

    @Override
    public Integer modifyleftProductMoney(LoanInfo loanInfo) {
        return loanInfoMapper.updateByPrimaryKeySelective(loanInfo);
    }

    /**
     * 立即投资
     * @param param
     */
    @Override
    @Transactional
    public void invest(HashMap param) throws Exception {
        Integer loanId = (Integer) param.get("loanId");
        Double bidMoney =(Double) param.get("bidMoney");
        Integer uid =(Integer) param.get("uid");
        String phone =(String)param.get("phone");

        //获取产品version
        LoanInfo loanInfo = loanInfoMapper.selectByPrimaryKey(loanId);
        Integer version = loanInfo.getVersion();
        param.put("version",version);

        //新增一条投资记录
        BidInfo bidInfo = new BidInfo();
        bidInfo.setLoanId(loanId);
        bidInfo.setUid(uid);
        bidInfo.setBidMoney(bidMoney);
        bidInfo.setBidTime(new Date());
        bidInfo.setBidStatus(1);
        int bidRows = bidInfoMapper.insertSelective(bidInfo);
        if (bidRows==0){
            throw new Exception("投资失败，添加投资记录错误");
        }

        //更新产品表中的剩余可投金额
        int Rows = loanInfoMapper.updateLeftProductMoneyByInvest(param);
        if (Rows==0){
            throw new Exception("投资失败，更新产品可投余额错误");
        }

        //更新账户余额
//        FinanceAccount financeAccount = financeAccountMapper.selectByPrimaryUid(uid);
//        Double availableMoney = financeAccount.getAvailableMoney();
//        financeAccount.setAvailableMoney(availableMoney-bidMoney);
        int faRows = financeAccountMapper.updateByInvest(param);
        if (faRows==0){
            throw new Exception("投资失败，更新账户余额错误");
        }

        //判断当前产品的剩余可投金额是否为0，如果是则更新状态为1
        loanInfo = loanInfoMapper.selectByPrimaryKey(loanId);
        if (loanInfo.getLeftProductMoney()==0){
            loanInfo.setProductStatus(1);
        }
        int loanRows = loanInfoMapper.updateByPrimaryKeySelective(loanInfo);
        if (loanRows==0){
            throw new Exception("投资失败，更新产品状态失败");
        }

        redisTemplate.opsForZSet().incrementScore(Constants.INVEST_TOP,phone,bidMoney);


    }
}
