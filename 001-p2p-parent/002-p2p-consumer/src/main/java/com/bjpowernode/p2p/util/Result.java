package com.bjpowernode.p2p.util;

import java.util.HashMap;
import java.util.Map;

public class Result extends HashMap<String,Object> {

    private Result(){}
    /**
     * 返回错误json结果数据
     *
     */
    public static Result successError(Map data){
        Result result = new Result();
        result.put("code", -1);
        result.put("message", data.get("message"));
        result.put("success", false);
        result.put("state", data.get("state"));
        return result;
    }
    /**
     * 返回成功json结果数据
     *
     */
    public static Result successOk(){
        Result result = new Result();
        result.put("code", 1);
        result.put("message", "");
        result.put("success", true);

        return result;
    }

    /**
     * 返回错误json结果数据
     *
     */
    public static Result successCode(String phoneCode){
        Result result = new Result();
        result.put("code", 1);
        result.put("phoneCode", phoneCode);
        return result;
    }
}
