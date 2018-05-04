package com.color.pdf.model;


import com.color.pdf.constant.ErrorCodeMapping;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

/**
 * 全局请求和响应返回对象
 * @author Color 2018.05.04
 */
public class GlobalRequestResult implements Serializable{
    private static final long serialVersionUID = -6071816845994211508L;

    public static final String errorMsgKey="errorMsg";

//    public static final Integer sucessCode=20000;//成功
//
//    public static final Integer noFindErrorCode=40000;//服务没有找到
//
//    public static final Integer exceptionErrorCode=40001;//异常
//
//    public static final Integer serviceErrorCode=50001;//服务错误编码
//
//    public static final Integer systemErrorCode=50002;//系统错误编码

    private Integer code=0;

    private Object data;


    public Integer getCode() {
        return code;
    }

    public void setCode(Integer code) {
        this.code = code;
    }

    public Object getData() {
        return data;
    }

    public void setData(Object data) {
        this.data = data;
    }


    public static GlobalRequestResult wrapSuccessResult(Object data){
        GlobalRequestResult  globalRequestResult = new GlobalRequestResult();
        globalRequestResult.setCode(ErrorCodeMapping.SYS_SUCCESS_CODE);
        globalRequestResult.setData(data);
        return globalRequestResult;
    }

    public static GlobalRequestResult wrapErrorResult(int code, String errorMsg){
        GlobalRequestResult  globalRequestResult = new GlobalRequestResult();
        globalRequestResult.setCode(code);
        Map<String,String> error = new HashMap<String,String>();
        error.put(errorMsgKey, errorMsg);
        globalRequestResult.setData(null);
        return globalRequestResult;
    }

}
