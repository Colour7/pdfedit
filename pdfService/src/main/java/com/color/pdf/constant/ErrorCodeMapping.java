package com.color.pdf.constant;


/**
 * 异常编码
 * @author Color 2018.05.04
 */
public class ErrorCodeMapping {

    public static final Integer SYS_SUCCESS_CODE = 20000;


    //***************************************************系统异常*********************************************************************
    public static final String SYS_ERROR_EXCEPTION_MESSAGE = "系统繁忙，请稍候再试";
    public static final Integer SYS_ERROR_EXCEPTION_CODE = -1;//系统繁忙，此时请稍候再试

    //***************************************************服务状态30000至39999*********************************************************************
    public static final Integer SERVICES_SYSTEM_EXCEPTION = 30000;//网关系统异常
    public static final Integer SERVICES_EXCEPTION = 30002;//业务异常
    public static final Integer SERIALIZABLE_EXCEPTION = 30003;//序列化异常

    //***************************************************业务异常40000~*********************************************************************
    public static final Integer PARAM_EXCEPTION = 40000;//参数异常

}
