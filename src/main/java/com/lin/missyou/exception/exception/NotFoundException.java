package com.lin.missyou.exception.exception;

import com.lin.missyou.exception.HttpException;

public class NotFoundException extends HttpException {
    public NotFoundException(int code) {
        this.code = code;
        this.httpStatusCode = 404;
    }
}
