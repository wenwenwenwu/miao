package com.lin.missyou.exception.exception;

import com.lin.missyou.exception.HttpException;

//登录了但无权访问
public class ForbiddenException extends HttpException {
    public ForbiddenException(int code) {
        this.code = code;
        this.httpStatusCode = 403;
    }
}
