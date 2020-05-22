package com.lin.missyou.exception.exception;

import com.lin.missyou.exception.HttpException;

//token异常
public class UnAuthenticatedException extends HttpException {
    public UnAuthenticatedException(int code) {
        this.code = code;
        this.httpStatusCode = 401;
    }
}
