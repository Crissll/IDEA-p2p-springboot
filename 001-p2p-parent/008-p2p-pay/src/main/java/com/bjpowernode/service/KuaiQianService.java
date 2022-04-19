package com.bjpowernode.service;

import com.alibaba.dubbo.config.annotation.Reference;
import com.bjpowernode.p2p.service.RechargeRecordService;
import com.bjpowernode.util.Pkipair;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;

/**
 * 处理付款后的处理结果
 */
@Service
public class KuaiQianService {

    @Reference(interfaceClass = RechargeRecordService.class,version = "1.0.0",timeout = 15000)
    private RechargeRecordService rechargeRecordService;

    public void handlerKQNotify(HttpServletRequest request) throws Exception {
        String merchantAcctId = request.getParameter("merchantAcctId");
        String version = request.getParameter("version");
        String language = request.getParameter("language");
        String signType = request.getParameter("signType");
        String payType = request.getParameter("payType");
        String bankId = request.getParameter("bankId");
        String orderId = request.getParameter("orderId");
        String orderTime = request.getParameter("orderTime");
        String orderAmount = request.getParameter("orderAmount");
        String bindCard = request.getParameter("bindCard");
//        String bindCard="";
        if(request.getParameter("bindCard")!=null){
            bindCard = request.getParameter("bindCard");}
        String bindMobile="";
        if(request.getParameter("bindMobile")!=null){
            bindMobile = request.getParameter("bindMobile");}
        String bankDealId = request.getParameter("bankDealId");
        String dealTime = request.getParameter("dealTime");
        String payAmount = request.getParameter("payAmount");
        String fee = request.getParameter("fee");
        String ext1 = request.getParameter("ext1");
        String ext2 = request.getParameter("ext2");
        String payResult = request.getParameter("payResult");
        String aggregatePay = request.getParameter("aggregatePay");
        String errCode = request.getParameter("errCode");
        String signMsg = request.getParameter("signMsg");
        String merchantSignMsgVal = "";
        merchantSignMsgVal = appendParam(merchantSignMsgVal,"merchantAcctId", merchantAcctId);
        merchantSignMsgVal = appendParam(merchantSignMsgVal, "version",version);
        merchantSignMsgVal = appendParam(merchantSignMsgVal, "language",language);
        merchantSignMsgVal = appendParam(merchantSignMsgVal, "signType",signType);
        merchantSignMsgVal = appendParam(merchantSignMsgVal, "payType",payType);
        merchantSignMsgVal = appendParam(merchantSignMsgVal, "bankId",bankId);
        merchantSignMsgVal = appendParam(merchantSignMsgVal, "orderId",orderId);
        merchantSignMsgVal = appendParam(merchantSignMsgVal, "orderTime",orderTime);
        merchantSignMsgVal = appendParam(merchantSignMsgVal, "orderAmount",orderAmount);
        merchantSignMsgVal = appendParam(merchantSignMsgVal, "bindCard",bindCard);
        merchantSignMsgVal = appendParam(merchantSignMsgVal, "bindMobile",bindMobile);
        //merchantSignMsgVal = appendParam(merchantSignMsgVal, "dealId",dealId);
        merchantSignMsgVal = appendParam(merchantSignMsgVal, "bankDealId",bankDealId);
        merchantSignMsgVal = appendParam(merchantSignMsgVal, "dealTime",dealTime);
        merchantSignMsgVal = appendParam(merchantSignMsgVal, "payAmount",payAmount);
        merchantSignMsgVal = appendParam(merchantSignMsgVal, "fee", fee);
        merchantSignMsgVal = appendParam(merchantSignMsgVal, "ext1", ext1);
        merchantSignMsgVal = appendParam(merchantSignMsgVal, "ext2", ext2);
        merchantSignMsgVal = appendParam(merchantSignMsgVal, "payResult",payResult);
        merchantSignMsgVal = appendParam(merchantSignMsgVal, "aggregatePay",aggregatePay);
        merchantSignMsgVal = appendParam(merchantSignMsgVal, "errCode",errCode);
        System.out.println("merchantSignMsgVal="+merchantSignMsgVal);
        Pkipair pki = new Pkipair();
        boolean flag = pki.enCodeByCer(merchantSignMsgVal, signMsg);
        int rtnOK =1;
        String rtnUrl="";
//        if(flag){
            //支付成功
            rechargeRecordService.doWithKQNotify(orderId,Integer.parseInt(payResult));
//        }else{
//            //支付失败
//
//        }
    }

    public String appendParam(String returns, String paramId, String paramValue) {
        if (!returns.equals("")) {

            if (paramValue != null && !paramValue.equals("")) {
                returns += "&" + paramId + "=" + paramValue;
            }

        } else {

            if (paramValue != null && !paramValue.equals("")) {
                returns = paramId + "=" + paramValue;
            }
        }

        return returns;
    }
}
