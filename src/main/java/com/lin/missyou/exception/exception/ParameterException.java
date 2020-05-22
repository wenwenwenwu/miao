package com.lin.missyou.exception.exception;

import com.lin.missyou.exception.HttpException;

public class ParameterException extends HttpException {
    public ParameterException(int code) {
        this.code = code;
        this.httpStatusCode = 400;
    }
}
