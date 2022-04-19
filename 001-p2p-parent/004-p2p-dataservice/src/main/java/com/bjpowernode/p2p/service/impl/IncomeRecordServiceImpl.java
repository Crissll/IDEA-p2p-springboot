package com.bjpowernode.p2p.service.impl;
import com.alibaba.dubbo.config.annotation.Service;
import com.bjpowernode.p2p.mapper.BidInfoMapper;
import com.bjpowernode.p2p.mapper.FinanceAccountMapper;
import com.bjpowernode.p2p.mapper.IncomeRecordMapper;
import com.bjpowernode.p2p.mapper.LoanInfoMapper;
import com.bjpowernode.p2p.model.loan.BidInfo;
import com.bjpowernode.p2p.model.loan.IncomeRecord;
import com.bjpowernode.p2p.model.loan.LoanInfo;
import com.bjpowernode.p2p.service.IncomeRecordService;
import org.apache.commons.lang3.time.DateUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Component
@Service(interfaceClass = IncomeRecordService.class,version = "1.0.0",timeout = 15000)
public class IncomeRecordServiceImpl implements IncomeRecordService {

    @Autowired
    private LoanInfoMapper loanInfoMapper;

    @Autowired
    private BidInfoMapper bidInfoMapper;

    @Autowired
    private IncomeRecordMapper incomeRecordMapper;

    @Autowired
    private FinanceAccountMapper financeAccountMapper;

    /**
     * 生成收益计划
     */
    @Override
    public void generateIncomePlan() throws Exception {
    //查询产品表状态是否满标
    List<LoanInfo> loanInfo =loanInfoMapper.selectLoanInfoByProductStatus(1);
    //遍历满标产品
        for (LoanInfo info : loanInfo) {
            //查找投资了该产品的用户信息
            List<BidInfo> bidInfo = bidInfoMapper.selectBidInfoByLoanId(info.getId());
            //遍历用户投资信息
            for (BidInfo UserBidInfo : bidInfo) {
                Date incomeDate=null;
                Double incomeMoney=null;
                //写入用户收益信息
                IncomeRecord incomeRecord = new IncomeRecord();
                incomeRecord.setUid(UserBidInfo.getUid());
                incomeRecord.setLoanId(UserBidInfo.getLoanId());
                incomeRecord.setBidId(UserBidInfo.getId());
                incomeRecord.setBidMoney(UserBidInfo.getBidMoney());
                if ("0".equals(info.getProductType())){
                    //收益时间=满标日期+产品周期
                    incomeDate = DateUtils.addDays(info.getProductFullTime(), info.getCycle());
                    //收益金额=本金*日利率*时间(天)
                    incomeMoney = UserBidInfo.getBidMoney()*(info.getRate()/100/365)*info.getCycle();
                }else {
                    //收益时间=满标日期+产品周期
                    incomeDate = DateUtils.addMonths(info.getProductFullTime(), info.getCycle());
                    //收益金额=本金*日利率*时间(天)
                    incomeMoney = UserBidInfo.getBidMoney()*(info.getRate()/100/365)*info.getCycle()*30;
                }
                //收益金额
                incomeMoney = Math.round(incomeMoney*Math.pow(10,2))/Math.pow(10,2);
                incomeRecord.setIncomeMoney(incomeMoney);
                //收益时间
                incomeRecord.setIncomeDate(incomeDate);

                incomeRecord.setIncomeStatus(0);
                int rows = incomeRecordMapper.insertSelective(incomeRecord);
                if (rows==0){
                    throw new Exception("生成收益计划失败");
                }
            }
            //生成投资计划后将产品状态由1改为2
            info.setProductStatus(2);
            int rows = loanInfoMapper.updateByPrimaryKeySelective(info);
            if (rows==0){
                throw  new Exception("修改产品状态失败");
            }
        }
    }

    /**
     * 返还收益
     */
    @Override
    public void generateIncomeMoney() throws Exception {
        //查询收益状态为0的记录
        List<IncomeRecord> incomeRecord = incomeRecordMapper.selectByIncomeStatus(0);
        for (IncomeRecord record : incomeRecord) {
            Map params = new HashMap();
            params.put("uid", record.getUid());
            params.put("incomeMoney", record.getIncomeMoney());
            params.put("bidMoney", record.getBidMoney());
            int rows = financeAccountMapper.updateByIncomeMoney(params);
            if (rows==0){
                throw new Exception("返还收益失败");
            }
            record.setIncomeStatus(1);
            int irRows = incomeRecordMapper.updateByPrimaryKeySelective(record);
            if (irRows==0){
                throw new Exception("更新投资记录状态失败");
            }
        }
    }
}
