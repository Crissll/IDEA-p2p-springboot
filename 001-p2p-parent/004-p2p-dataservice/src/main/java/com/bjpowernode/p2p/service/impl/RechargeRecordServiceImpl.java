package com.bjpowernode.p2p.service.impl;

import com.alibaba.dubbo.config.annotation.Service;
import com.bjpowernode.p2p.mapper.FinanceAccountMapper;
import com.bjpowernode.p2p.mapper.RechargeRecordMapper;
import com.bjpowernode.p2p.model.loan.RechargeRecord;
import com.bjpowernode.p2p.service.RechargeRecordService;
import org.apache.commons.lang3.ObjectUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Map;
@Component
@Service(interfaceClass = RechargeRecordService.class,version = "1.0.0",timeout = 15000)
public class RechargeRecordServiceImpl implements RechargeRecordService {
    @Autowired
    private RechargeRecordMapper rechargeRecordMapper;

    @Autowired
    private FinanceAccountMapper financeAccountMapper;

    @Override
    public RechargeRecord queryRechargeRecordByUid(Map params) {
        return rechargeRecordMapper.selectRechargeRecordByUid(params);
    }

    @Override
    public int addRechargeRecode(RechargeRecord rechargeRecord) {
        return rechargeRecordMapper.insertSelective(rechargeRecord);
    }

    @Override
    public int doWithKQNotify(String orderId, Integer payResult) throws Exception {
        //1. 查询订单号是否商家订单
        RechargeRecord rechargeRecord = rechargeRecordMapper.selectByPrimaryNo(orderId);
        if (ObjectUtils.allNotNull(rechargeRecord)){
            //2. 判断订单是否处理过，没有处理过的才能处理
            String rechargeStatus = rechargeRecord.getRechargeStatus();
            if (rechargeStatus.equals("0")){
                //3. 更新账户余额
                int rows = financeAccountMapper.updateByprimaryPayMoney(rechargeRecord.getRechargeMoney(),rechargeRecord.getUid());
                if (rows==0){
                    throw new Exception("充值失败");
                }
            }
        }else {
            throw new Exception("查询充值订单失败");
        }
        //4. 修改充值表中的充值状态
        rechargeRecord.setRechargeStatus("1");
        int reRows = rechargeRecordMapper.updateByPrimaryKeySelective(rechargeRecord);
        if (reRows==0){
            throw new Exception("修改充值状态失败");
        }
        return 0;
    }
}
