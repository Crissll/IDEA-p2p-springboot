package com.bjpowernode.web;
import com.alibaba.dubbo.config.annotation.Reference;
import com.bjpowernode.p2p.cons.Constants;
import com.bjpowernode.p2p.model.loan.LoanInfo;
import com.bjpowernode.p2p.service.BidInfoService;
import com.bjpowernode.p2p.service.LoanInfoService;
import com.bjpowernode.p2p.service.UserService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.HashMap;
import java.util.List;

@Controller
public class IndexController {

    @Reference(interfaceClass = LoanInfoService.class ,version = "1.0.0",check = false,timeout = 15000)
    private LoanInfoService loanInfoService;

    @Reference(interfaceClass = UserService.class,version = "1.0.0",check = false,timeout=15000)
    private UserService UserService;

    @Reference(interfaceClass = BidInfoService.class,version = "1.0.0" ,check = false,timeout = 15000)
    private BidInfoService bidInfoService;

    @RequestMapping("/index")
    public String index(Model model){

        //获取平台平均年化收益率
        Double historyAvgRate = loanInfoService.loanInfoHistoryAvgRate();
        model.addAttribute(Constants.HISTORY_AVG_PATE,historyAvgRate);


        //平台总用户数
        Integer allUserCount = UserService.queryAllUserCount();
        model.addAttribute(Constants.ALL_USER_COUNT, allUserCount);

        //获取平台累计成交金额
        Double allBidMoney = bidInfoService.queryAllBidMoney();
        model.addAttribute(Constants.ALL_BID_MONEY, allBidMoney);

        //获取新手宝信息
        HashMap<String,Object> params = new HashMap();
        params.put("productType", Constants.PRODUCT_TYPE);
        params.put("currentPage",0 );
        params.put("pageSize", 1);
        List<LoanInfo> loanInfoList = loanInfoService.queryLoanInfoListProductType(params);
        model.addAttribute(Constants.LOAN_INFO_LIST, loanInfoList);

        //获取优选产品信息
        params.put("productType", Constants.PRODUCT_TYPE_X);
        params.put("currentPage",0 );
        params.put("pageSize", 4);
        List<LoanInfo> loanInfoListX = loanInfoService.queryLoanInfoListProductType(params);
        model.addAttribute(Constants.LOAN_INFO_LIST_X, loanInfoListX);

        //获取散标类型的产品信息
        params.put("productType", Constants.PRODUCT_TYPE_S);
        params.put("currentPage",0 );
        params.put("pageSize", 8);
        List<LoanInfo> loanInfoListS = loanInfoService.queryLoanInfoListProductType(params);
        model.addAttribute(Constants.LOAN_INFO_LIST_S, loanInfoListS);
        return "index";
    }
}
