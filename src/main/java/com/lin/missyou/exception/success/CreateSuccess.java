package com.lin.missyou.exception.success;

import com.lin.missyou.exception.HttpException;

public class CreateSuccess extends HttpException {
    public CreateSuccess(int code){
        this.httpStatusCode = 201;
        this.code = code;
    }
}
