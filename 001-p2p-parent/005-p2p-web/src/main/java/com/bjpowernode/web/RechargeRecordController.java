package com.bjpowernode.web;
import com.alibaba.dubbo.config.annotation.Reference;
import com.bjpowernode.p2p.cons.Constants;
import com.bjpowernode.p2p.model.loan.RechargeRecord;
import com.bjpowernode.p2p.model.user.User;
import com.bjpowernode.p2p.service.KuaiQianService;
import com.bjpowernode.p2p.service.RechargeRecordService;
import org.apache.commons.lang3.time.DateFormatUtils;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import javax.servlet.http.HttpServletRequest;
import java.util.Date;
import java.util.Map;

@Controller
public class RechargeRecordController {
    @Reference(interfaceClass = KuaiQianService.class,version = "1.0.0",check = false,timeout = 15000)
    private KuaiQianService kuaiQianService;

    @Reference(interfaceClass = RechargeRecordService.class,version = "1.0.0",check = false,timeout = 1500)
    private RechargeRecordService rechargeRecordService;

    @RequestMapping("loan/page/toRecharge")
    public String toRecharge(){
        return "toRecharge";
    }

    /**
     * 支付
     * @param request
     * @param model
     * @param rechargeMoney
     * @return
     */
    @RequestMapping("/loan/toRecharge")
    public String submitRecharge(HttpServletRequest request,
                                 Model model,
                                 @RequestParam(value = "rechargeMoney",required = true)String rechargeMoney
                                 ) throws Exception {
        User user = (User) request.getSession().getAttribute(Constants.SESSION_USER);
        if (user==null){
            return "redirect:/loan/page/login";
        }
        //生成充值记录
        RechargeRecord rechargeRecord = new RechargeRecord();
        rechargeRecord.setUid(user.getId());
        String OrderId = generateOrderId();
        rechargeRecord.setRechargeNo(OrderId);
        rechargeRecord.setRechargeStatus("0");
        rechargeRecord.setRechargeMoney(Double.valueOf(rechargeMoney));
        rechargeRecord.setRechargeTime(new Date());
        rechargeRecord.setRechargeDesc("快钱支付");
        int rows = rechargeRecordService.addRechargeRecode(rechargeRecord);
        if (rows==0){
            model.addAttribute("target_msg", "新增充值记录失败");
            return "toRechargeBack";
        }


        Map map = kuaiQianService.makeKuaiQianRequestParam(rechargeMoney,user.getName(),user.getPhone(),OrderId);
        model.addAllAttributes(map);
        return "kuaiQianForm";
    }

    private String generateOrderId() {
        return "KQ" + DateFormatUtils.format(new Date(),"yyyyMMddHHmmssSSS");
    }
}
