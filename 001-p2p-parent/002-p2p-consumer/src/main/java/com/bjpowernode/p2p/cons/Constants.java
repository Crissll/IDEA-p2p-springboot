package com.bjpowernode.p2p.cons;

import com.sun.org.apache.bcel.internal.generic.NEW;

import java.util.HashMap;
import java.util.Map;

public class Constants {
    /**
     * 平均年化收益率
     */
    public static final String HISTORY_AVG_PATE="historyAvgRate";


    public static final String ALL_USER_COUNT="allUserCount";

    public static final String ALL_BID_MONEY="allBidMoney";

    /**
     * 新手宝
     */

    public static final String LOAN_INFO_LIST="loanInfoList";
    public static final Integer PRODUCT_TYPE=0;

    /**
     * 新手宝优选
     */
    public static final String LOAN_INFO_LIST_X="loanInfoListX";
    public static final Integer PRODUCT_TYPE_X=1;

    /**
     * 散标
     */
    public static final String LOAN_INFO_LIST_S="loanInfoListS";
    public static final Integer PRODUCT_TYPE_S=2;

    /**
     * session对象
     */
    public static final String SESSION_USER="sessionUser";

    /**
     * 注册活动奖励
     */
    public static final Double ACTIVITY_REWARD=888.0;

    /**
     * 发送短信验证码失败
     */
    public static final Map<String ,Object> ERROR_STATE_1=new HashMap<String ,Object>(){
        {
            put("state", -1);
            put("message", "发送短信验证码失败");
        }
    };
    /**
     * 注册失败
     */
    public static final Map<String ,Object> ERROR_STATE_2=new HashMap<String ,Object>(){
        {
            put("state", -2);
            put("message", "注册失败");
        }
    };
    /**
     * 系统繁忙，注册失败
     */
    public static final Map<String ,Object> ERROR_STATE_3=new HashMap<String ,Object>(){
        {
            put("state", -3);
            put("message", "系统繁忙，注册失败");
        }
    };
    /**
     * 验证码错误
     */
    public static final Map<String ,Object> ERROR_STATE_4=new HashMap<String ,Object>(){
        {
            put("state", -1);
            put("message", "验证码错误");
        }
    };

    /**
     * 认证失败
     */
    public static final Map<String ,Object> ERROR_STATE_5=new HashMap<String ,Object>(){
        {
            put("state", -1);
            put("message", "认证失败");
        }
    };
    /**
     * 没有登入
     */
    public static final Map<String ,Object> ERROR_STATE_6=new HashMap<String ,Object>(){
        {
            put("state", -1);
            put("message", "请先登入");
        }
    };
    /**
     * 登入失败
     */
    public static final Map<String ,Object> ERROR_STATE_7=new HashMap<String ,Object>(){
        {
            put("state", -1);
            put("message", "登入失败,账号或密码错误");
        }
    };
    /**
     * 投资失败
     */
    public static final Map<String ,Object> ERROR_STATE_8=new HashMap<String ,Object>(){
        {
            put("state", -1);
            put("message", "投资失败");
        }
    };

    /**
     * 投资排行榜
     */
    public static final String INVEST_TOP="invest_top";


}
