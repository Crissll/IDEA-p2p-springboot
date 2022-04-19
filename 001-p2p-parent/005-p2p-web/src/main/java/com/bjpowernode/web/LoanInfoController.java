package com.bjpowernode.web;

import com.alibaba.dubbo.config.annotation.Reference;
import com.alibaba.dubbo.rpc.support.RpcUtils;
import com.bjpowernode.p2p.cons.Constants;
import com.bjpowernode.p2p.model.loan.BidInfo;
import com.bjpowernode.p2p.model.loan.LoanInfo;
import com.bjpowernode.p2p.model.user.FinanceAccount;
import com.bjpowernode.p2p.model.user.User;
import com.bjpowernode.p2p.model.vo.BidUserVOList;
import com.bjpowernode.p2p.model.vo.PaginationVO;
import com.bjpowernode.p2p.service.BidInfoService;
import com.bjpowernode.p2p.service.FinanceAccountService;
import com.bjpowernode.p2p.service.LoanInfoService;
import com.bjpowernode.p2p.service.RechargeRecordService;
import com.bjpowernode.p2p.util.Result;
import org.apache.commons.lang3.ObjectUtils;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
public class LoanInfoController {
    @Reference(interfaceClass = LoanInfoService.class,version = "1.0.0",check = false,timeout = 15000)
    private  LoanInfoService loanInfoService;

    @Reference(interfaceClass = BidInfoService.class,version = "1.0.0",check = false,timeout = 15000)
    private BidInfoService bidInfoService;

    @Reference(interfaceClass = FinanceAccountService.class,version = "1.0.0",check = false,timeout = 15000)
    private FinanceAccountService financeAccountService;


    @RequestMapping("loan/loan")
    public String loan(@RequestParam(value = "ptype",required = false) Integer ptype,
                       @RequestParam(value = "currentPage",defaultValue = "1" ) Integer currentPage,
                       Model model
                       ){

        //根据产品类型，分页查询产品列表 queryLoanInfoListProductType(参数产品类型可为空，currentPage当前页，pageSize每页多少条数据)
        //分页类 List<T> 分页数据 totalSize总页数
        Integer pageSize = 9;
        HashMap<String,Object> params = new HashMap();
        if (ObjectUtils.allNotNull(ptype)){
            params.put("productType",ptype);
        }
        params.put("pageSize",pageSize);
        //计算查询条件从第几条数据开始查
        params.put("currentPage", (currentPage-1)*pageSize);
        PaginationVO paginationVO = loanInfoService.queryLoanInfoListByPage(params);

        //传入查询数据
        model.addAttribute("loanInfoList", paginationVO.getDatas());
        //总条数
        model.addAttribute("pageSize",paginationVO.getTotalSize());
        //计算总页数
        Integer totalPage = paginationVO.getTotalSize()/pageSize;
        if (paginationVO.getTotalSize()%pageSize!=0){
            totalPage+=1;
        }
        model.addAttribute("totalPage", totalPage);
        model.addAttribute("currentPage", currentPage);

        if (ObjectUtils.allNotNull(ptype)){
            model.addAttribute("ptype",ptype);
        }

        //  TODO 投资排行榜
        List<BidUserVOList> bidUserVOList = bidInfoService.queryBidUserTop();
        model.addAttribute("bidUserVOList",bidUserVOList);
        return "loan";
    }

    @RequestMapping("loan/loanInfo")
    public String LoanInfo(@RequestParam(value = "loanId",required = true) Integer loanId, Model model, HttpServletRequest request){
        HttpSession session = request.getSession();
        User user = (User) session.getAttribute(Constants.SESSION_USER);
        if (ObjectUtils.allNotNull(user)){
            FinanceAccount financeAccount = financeAccountService.queryFianceAccountByUid(user.getId());
            model.addAttribute("financeAccount", financeAccount);
        }
//        else {
//            return "redirect:/loan/page/login";
//        }

        //查询对应产品的详细信息
        LoanInfo loanInfo = loanInfoService.queryLoanInfoProductData(loanId);
        model.addAttribute("loanInfo",loanInfo);

        //用户产品投资历史记录
        Map params = new HashMap();
        params.put("loanId", loanId);
        params.put("currentPage", 0);
        params.put("pageSize", 10);
        List<BidInfo> bidInfo = bidInfoService.queryRecentlyByLoanId(params);
        model.addAttribute("bidInfo",bidInfo);



        return "loanInfo";
    }

    @RequestMapping("loan/invest")
    @ResponseBody
    public Map invest(HttpServletRequest request,@RequestParam(value = "bidMoney",required = true) Double bidMoney,
                      @RequestParam(value = "loanId",required = true)Integer loanId
                      ){
        try {

            //新增一条投资记录
            //更新产品表中的剩余可投金额
            //判断当前产品的剩余可投金额是否为0，如果是则更新状态为1
            HttpSession session = request.getSession();
            User user = (User) session.getAttribute(Constants.SESSION_USER);

            HashMap param = new HashMap();
            param.put("loanId", loanId);
            param.put("uid", user.getId());
            param.put("bidMoney", bidMoney);
            param.put("phone", user.getPhone());
            loanInfoService.invest(param);

        }catch (Exception e){
            return Result.successError(Constants.ERROR_STATE_8);
        }
        return Result.successOk();
    }

    @RequestMapping("loan/myInvest")
    @ResponseBody
    public String myInvest(){
        return "myInvest";
    }
}
