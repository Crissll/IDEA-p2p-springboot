package com.bjpowernode.web;

import com.alibaba.dubbo.config.annotation.Reference;
import com.bjpowernode.p2p.cons.Constants;
import com.bjpowernode.p2p.model.user.FinanceAccount;
import com.bjpowernode.p2p.model.user.User;
import com.bjpowernode.p2p.service.FinanceAccountService;
import org.apache.commons.lang3.ObjectUtils;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

@Controller
public class FinanceAccountController {

    @Reference(interfaceClass = FinanceAccountService.class,version = "1.0.0",check = false,timeout = 15000)
    private FinanceAccountService financeAccountService;

    @RequestMapping("/loan/myFinanceAccount")
    @ResponseBody
    public FinanceAccount myFinanceAccount(HttpServletRequest request){
        HttpSession session = request.getSession();
        User user = (User) session.getAttribute(Constants.SESSION_USER);
        FinanceAccount financeAccount = null;
        if (ObjectUtils.allNotNull(user)){
            Integer id = user.getId();
            financeAccount = financeAccountService.queryFianceAccountByUid(id);
        }
        return financeAccount;
    }

}
