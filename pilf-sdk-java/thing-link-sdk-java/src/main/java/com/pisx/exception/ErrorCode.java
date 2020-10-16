package com.pisx.exception;

public class ErrorCode {
    public static final int SUCCESS                              = 0;
    public static final int ERROR_UNKNOWN                        = 100000;
    public static final int ERROR_INVALID_PARAM                  = 100001;
    public static final int ERROR_TIMEOUT                        = 100006;
    public static final int ERROR_PARAM_RANGE_OVERFLOW           = 100007;
    public static final int ERROR_SERVICE_UNREACHABLE            = 100008;
    public static final int ERROR_DEVICE_UNREGISTER            = 109000;
    public static final int ERROR_DEVICE_OFFLINE               = 109001;
    public static final int ERROR_PROPERTY_NOT_EXIST           = 109002;
    public static final int ERROR_PROPERTY_READ_ONLY           = 109003;
    public static final int ERROR_PROPERTY_WRITE_ONLY          = 109004;
    public static final int ERROR_SERVICE_NOT_EXIST            = 109005;
    public static final int ERROR_SERVICE_INPUT_PARAM          = 109006;
    public static final int ERROR_INVALID_JSON                 = 109007;
    public static final int ERROR_INVALID_TYPE                 = 109008;

    private static final String SUCCESS_MSG                      = "Success";                          /* 请求成功*/
    private static final String ERROR_UNKNOWN_MSG                = "Unknown error";                    /* 不能被识别的错误，用户不应该看到的错误*/
    private static final String ERROR_INVALID_PARAM_MSG          = "Invalid params";                   /* 传入参数为NULL或无效*/
    private static final String ERROR_TIMEOUT_MSG                = "Tiemout";                          /* 超时*/
    private static final String ERROR_PARAM_RANGE_OVERFLOW_MSG   = "Param range overflow";             /* 参数范围越界*/
    private static final String ERROR_SERVICE_UNREACHABMSG    = "Service unreachable";              /* 服务不可达*/

    private static final String ERROR_DEVICE_UNREGISTER_MSG    = "Device has't register";            /* 设备未注册*/
    private static final String ERROR_DEVICE_OFFLINE_MSG       = "Device has offline";               /* 设备已下线*/
    private static final String ERROR_PROPERTY_NOT_EXIST_MSG   = "Property no exist";                /* 属性不存在*/
    private static final String ERROR_PROPERTY_READ_ONLY_MSG   = "Property only support read";       /* 属性只读*/
    private static final String ERROR_PROPERTY_WRITE_ONLY_MSG  = "Property only support write";      /* 属性只写*/
    private static final String ERROR_SERVICE_NOT_EXIST_MSG    = "Service no exist";                 /* 服务不存在*/
    private static final String ERROR_SERVICE_INPUT_PARAM_MSG  = "Service param invalid";            /* 服务的输入参数不正确错误码*/
    private static final String ERROR_INVALID_JSON_MSG         = "Json format invalid";              /* JSON格式错误*/
    private static final String ERROR_INVALID_TYPE_MSG         = "Param type invalid";               /* 参数类型错误*/

    public String getMessage(int code) {
        String message = null;
        switch (code) {
            case SUCCESS:
                message = SUCCESS_MSG;
                break;
            case ERROR_UNKNOWN:
                message = ERROR_UNKNOWN_MSG;
                break;
            case ERROR_INVALID_PARAM:
                message = ERROR_INVALID_PARAM_MSG;
                break;
            case ERROR_TIMEOUT:
                message = ERROR_TIMEOUT_MSG;
                break;
            case ERROR_PARAM_RANGE_OVERFLOW:
                message = ERROR_PARAM_RANGE_OVERFLOW_MSG;
                break;
            case ERROR_SERVICE_UNREACHABLE:
                message = ERROR_SERVICE_UNREACHABMSG;
                break;
            case ERROR_DEVICE_UNREGISTER:
                message = ERROR_DEVICE_UNREGISTER_MSG;
                break;
            case ERROR_DEVICE_OFFLINE:
                message = ERROR_DEVICE_OFFLINE_MSG;
                break;
            case ERROR_PROPERTY_NOT_EXIST:
                message = ERROR_PROPERTY_NOT_EXIST_MSG;
                break;
            case ERROR_PROPERTY_READ_ONLY:
                message = ERROR_PROPERTY_READ_ONLY_MSG;
                break;
            case ERROR_PROPERTY_WRITE_ONLY:
                message = ERROR_PROPERTY_WRITE_ONLY_MSG;
                break;
            case ERROR_SERVICE_NOT_EXIST:
                message = ERROR_SERVICE_NOT_EXIST_MSG;
                break;
            case ERROR_SERVICE_INPUT_PARAM:
                message = ERROR_SERVICE_INPUT_PARAM_MSG;
                break;
            case ERROR_INVALID_JSON:
                message = ERROR_INVALID_JSON_MSG;
                break;
            case ERROR_INVALID_TYPE:
                message = ERROR_INVALID_TYPE_MSG;
                break;
            default:
                break;
        }

        return message;
    }
}