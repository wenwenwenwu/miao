package com.lin.missyou.exception;

public class HttpException extends RuntimeException {
    protected Integer code; //自定义错误码
    protected Integer httpStatusCode; //HTTP状态码

    public Integer getCode() {
        return code;
    }

    public Integer getHttpStatusCode() {
        return httpStatusCode;
    }
}
