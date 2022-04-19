package com.bjpowernode.p2p.service;
import com.bjpowernode.p2p.model.loan.RechargeRecord;
import java.util.Map;

public interface RechargeRecordService {
    /**
     * 根据用户uid查询充值记录
     * @param params
     * @return
     */
    RechargeRecord queryRechargeRecordByUid(Map params);

    /**
     *生成充值记录
     * @param rechargeRecord
     * @return
     */
    int addRechargeRecode(RechargeRecord rechargeRecord);

    /**
     * 处理快钱异步通知
     * @param orderId 商家订单号
     * @param payResult 快钱给的支付结果
     * @return int 处理结果
     */
    int doWithKQNotify(String orderId, Integer payResult) throws Exception;
}
