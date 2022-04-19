package com.bjpowernode.web;
import com.alibaba.dubbo.config.annotation.Reference;
import com.alibaba.fastjson.JSONObject;
import com.bjpowernode.p2p.cons.Constants;
import com.bjpowernode.p2p.model.loan.BidInfo;
import com.bjpowernode.p2p.model.loan.LoanInfo;
import com.bjpowernode.p2p.model.loan.RechargeRecord;
import com.bjpowernode.p2p.model.user.FinanceAccount;
import com.bjpowernode.p2p.model.user.User;
import com.bjpowernode.p2p.service.*;
import com.bjpowernode.p2p.util.Result;
import org.apache.commons.lang3.ObjectUtils;
import org.apache.commons.lang3.StringUtils;
import org.dom4j.Document;
import org.dom4j.DocumentHelper;
import org.dom4j.Node;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.*;
import java.util.concurrent.TimeUnit;

@Controller
public class UserController {

    @Reference(interfaceClass = UserService.class,version = "1.0.0",check = false,timeout = 15000)
    private UserService userService;

    @Reference(interfaceClass = RedisService.class,version = "1.0.0",check = false,timeout = 15000)
    private RedisService redisService;

    @Reference(interfaceClass = FinanceAccountService.class,version = "1.0.0",check = false,timeout = 15000)
    private FinanceAccountService financeAccountService;

    @Reference(interfaceClass = BidInfoService.class,version = "1.0.0",check = false,timeout = 15000)
    private BidInfoService bidInfoService;

    @Reference(interfaceClass = LoanInfoService.class,version = "1.0.0",check = false,timeout = 15000)
    private LoanInfoService loanInfoService;


    @Reference(interfaceClass = RechargeRecordService.class,version = "1.0.0",check = false,timeout = 15000)
    private RechargeRecordService rechargeRecordService;

    @RequestMapping("loan/page/register")
    public String toRegister(){
        return "register";
    }

    /**
     * 验证手机号是否存在
     * @param phone
     * @return
     */
    @RequestMapping("/loan/checkPhone")
    @ResponseBody
    public Map queryUserByPhone(@RequestParam(value = "phone",required =true) String phone){
//        User user = userService.queryUserByPhone(phone);
//        if (user == null){
//            return  Result.successOk();
//        }else{
//            return Result.successError("手机号"+phone+"已经存在");
//        }
        HashMap map = new HashMap();
        User user = userService.queryUserByPhone(phone);
        if (user == null){
            map.put("data", Result.successOk());
        }else{
            HashMap param = new HashMap();
            param.put("message", "手机号"+phone+"已经存在");
            map.put("data", Result.successError(param));
        }
        return map;
    }

    /**
     * 注册
     * @param request
     * @param phone
     * @param loginPassword
     * @param messageCode
     * @return
     */
    @RequestMapping("loan/register")
    @ResponseBody
    public Map addUser(HttpServletRequest request, @RequestParam(value = "phone",required = true) String phone,
                       @RequestParam(value = "loginPassword",required = true) String loginPassword,
                       @RequestParam(value = "messageCode",required = true) String messageCode
                           ){
        String RedisCode = (String)redisService.getRedisCode(phone);
        if (!StringUtils.equals(RedisCode, messageCode)){
            return Result.successError(Constants.ERROR_STATE_4);
        }

        try {
            User user = userService.addUser(phone,loginPassword);
            System.out.println(user);
            HttpSession session = request.getSession();
            session.setAttribute(Constants.SESSION_USER,user);

            User attribute = (User)session.getAttribute(Constants.SESSION_USER);
            System.out.println(attribute);

        }catch (Exception e){
            return Result.successError(Constants.ERROR_STATE_3);
        }

        return Result.successOk();
    }

    /**
     * 验证码发送
     * @param phone
     * @return
     * @throws Exception
     */
    @RequestMapping("user/messageCode")
    @ResponseBody
    public Map sendMessageCode(@RequestParam(value = "phone",required = true) String phone) throws Exception {
        //封装数据请求第三方接口发送验证码
        String RandomCode = getRandomCode(6);

        HashMap param = new HashMap();
        param.put("appkey","7c026a5812255d003235dc5e4fe71906");
        param.put("mobile",phone);
        param.put("content", "【凯信通】您的验证码是："+RandomCode);
//        String result = HttpClientUtils.doGet("https://way.jd.com/kaixintong/kaixintong", param);
        String result ="{\n" +
                "\t\t\t\t\t\"code\": \"10000\",\n" +
                "\t\t\t\t\t\"charge\": false,\n" +
                "\t\t\t\t\t\"remain\": 0,\n" +
                "\t\t\t\t\t\"msg\": \"查询成功\",\n" +
                "\t\t\t\t\t\"result\": \"<?xml version=\\\"1.0\\\" encoding=\\\"utf-8\\\" ?><returnsms>\\n <returnstatus>Success</returnstatus>\\n <message>ok</message>\\n <remainpoint>-6850193</remainpoint>\\n <taskID>162317289</taskID>\\n <successCounts>1</successCounts></returnsms>\"\n" +
                "\t\t\t\t\t}";
        JSONObject jsonObject = JSONObject.parseObject(result);
        String code = jsonObject.getString("code");
        if (!StringUtils.equals(code, "10000")){
            return Result.successError(Constants.ERROR_STATE_1);
        }

        String resultxml = jsonObject.getString("result");
        Document document = DocumentHelper.parseText(resultxml);
        Node node = document.selectSingleNode("//returnstatus");
        if (!StringUtils.equals(node.getText(), "Success")){
            return Result.successError(Constants.ERROR_STATE_1);
        }
//        //短信发送成功，将验证码保存到redis
        redisService.put(phone,RandomCode,15, TimeUnit.MINUTES);
        return Result.successCode(RandomCode);
    }


    /**
     * 实名认证页面跳转
     * @return
     */
    @RequestMapping("loan/page/realName")
    public String toRealName(){
        return "realName";
    }

    /**
     * 实名认证校验
     * @param phone
     * @param realName
     * @param idCard
     * @param messageCode
     * @param request
     * @return
     * @throws Exception
     */
    @RequestMapping("/loan/realName")
    @ResponseBody
    public Map realName(@RequestParam(value = "phone",required = true) String phone,
                        @RequestParam(value = "realName",required = true) String realName,
                        @RequestParam(value = "idCard", required = true) String idCard,
                        @RequestParam(value = "messageCode",required = true)String messageCode,
                        HttpServletRequest request
                        ) throws Exception {

        //验证码是否正确
        try {
            String redisCode = (String)redisService.getRedisCode(phone);
            if (!StringUtils.equals(redisCode, messageCode)){
                return Result.successError(Constants.ERROR_STATE_4);
            }
            HashMap params = new HashMap();
            params.put("appkey", "7c026a5812255d003235dc5e4fe71906");
            params.put("cardNo", idCard);
            params.put("realName", realName);
//            String json = HttpClientUtils.doPost("https://way.jd.com/hl/checkidcard", params);
            String json="{\n" +
                    "    \"code\": \"10000\",\n" +
                    "    \"charge\": false,\n" +
                    "    \"remain\": 1305,\n" +
                    "    \"msg\": \"查询成功\",\n" +
                    "    \"result\": {\n" +
                    "        \"error_code\": 0,\n" +
                    "        \"reason\": \"成功\",\n" +
                    "        \"result\": {\n" +
                    "            \"realname\": \"乐天磊\",\n" +
                    "            \"idcard\": \"350721197702134399\",\n" +
                    "            \"isok\": true\n" +
                    "        }\n" +
                    "    }\n" +
                    "}";
            JSONObject result = JSONObject.parseObject(json);
            String code = result.getString("code");
            if (!StringUtils.equals(code, "10000")){
                return Result.successError(Constants.ERROR_STATE_5);
            }
            String stateResult = result.getString("result");
            JSONObject toResult = JSONObject.parseObject(stateResult);
            String resultRealName = toResult.getString("result");
            JSONObject RealName = JSONObject.parseObject(resultRealName);
            String isok = RealName.getString("isok");
            if (!"true".equals(isok)){
                return Result.successError(Constants.ERROR_STATE_5);
            }

            //存储认证信息
            HttpSession session = request.getSession();
            User user = (User) session.getAttribute(Constants.SESSION_USER);
            if (ObjectUtils.allNotNull(user)){
                user.setName(realName);
                user.setIdCard(idCard);
                int rows = userService.modifyUserByRealName(user);
                if (rows!=1){
                    return Result.successError(Constants.ERROR_STATE_5);
                }
            }else {
                return Result.successError(Constants.ERROR_STATE_6);
            }
        }catch (Exception e){
            return Result.successError(Constants.ERROR_STATE_5);
        }


        return Result.successOk();
    }


    /**
     * 退出登入
     * @param request
     * @return
     */
    @RequestMapping("/loan/logout")
    public String logout(HttpServletRequest request){
        HttpSession session = request.getSession();
        session.removeAttribute(Constants.SESSION_USER);
        return "redirect:/index";
    }

    /**
     * 跳转登入页面
     * @return
     */
    @RequestMapping("/loan/page/login")
    public String tologin(){
        return "login";
    }

    /**
     * 登入页面
     * @param phone
     * @param loginPassword
     * @param request
     * @return
     */
    @RequestMapping("/loan/login")
    @ResponseBody
    public Map login(@RequestParam(value = "phone",required = true) String phone,
                      @RequestParam(value = "loginPassword",required = true)String loginPassword,
                     HttpServletRequest request
                    ){
        User user = userService.login(phone,loginPassword);
        if (ObjectUtils.allNotNull(user)){
            HttpSession session = request.getSession();
            session.setAttribute(Constants.SESSION_USER,user);
            user.setLastLoginTime(new Date());
            return Result.successOk();
        }
        return Result.successError(Constants.ERROR_STATE_7);
    }

    /**
     * 个人信息页面（小金库）
     * @return
     */
    @RequestMapping("loan/myCenter")
    public String toMyCenter(HttpServletRequest request, Model model){

        //查询用户是否存在
        HttpSession session = request.getSession();
        User user = (User) session.getAttribute(Constants.SESSION_USER);
        if (!ObjectUtils.allNotNull(user)){
            //用户不存在调转到登入页面
            return "redirect:/loan/page/login";
        }
        FinanceAccount financeAccount = financeAccountService.queryFianceAccountByUid(user.getId());
        model.addAttribute("financeAccount",financeAccount);

        //查询最近投资记录
        HashMap params = new HashMap();
        params.put("id", user.getId());
        params.put("currentPage", 0);
        params.put("pageSize", 5);
        List<BidInfo> bidRecord = bidInfoService.queryAllBidRecord(params);
        if (ObjectUtils.allNotNull(bidRecord)){
            model.addAttribute("bidRecord",bidRecord);
            if (bidRecord.size()>0){
                BidInfo bidInfo = bidRecord.get(0);
                //获取产品id查询产品信息
                Integer loanId = bidInfo.getLoanId();
                LoanInfo loanInfo = loanInfoService.queryLoanInfoProductData(loanId);
                model.addAttribute("loanInfo",loanInfo);
            }
        }
        //用户最近充值记录
//        RechargeRecord rechargeRecord = rechargeRecordService.queryRechargeRecordByUid(params);
//        model.addAttribute("rechargeRecord",rechargeRecord);
        //用户最近收益记录

        return "myCenter";
    }




    //生成随机验证码
    private String getRandomCode(int n){
        StringBuffer stringBuffer = new StringBuffer();
        Random random = new Random();
        for (int i = 0; i <n ; i++) {
            int i1 = random.nextInt(10);
            stringBuffer.append(i1);
        }
        return stringBuffer.toString();
    }
}
