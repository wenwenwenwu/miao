package com.lin.missyou.core;

import com.lin.missyou.core.configuration.ExceptionCodeConfiguration;
import com.lin.missyou.exception.HttpException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

import javax.servlet.http.HttpServletRequest;
import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;
import java.util.List;

@ControllerAdvice
public class GlobalExceptionAdvice {
    @Autowired
    private ExceptionCodeConfiguration codeConfiguration;

    //未知异常
    @ExceptionHandler(Exception.class)
    @ResponseBody
    @ResponseStatus(code = HttpStatus.INTERNAL_SERVER_ERROR)
    public UnifyResponse handleException(HttpServletRequest req, Exception e) {
        System.out.println(e);
        String requestURI = req.getRequestURI();
        String method = req.getMethod();
        UnifyResponse response = new UnifyResponse(9999, codeConfiguration.getMessage(9999), this.request(method,requestURI));
        return response;
    }

    //HttpStatusCode异常
    @ExceptionHandler(HttpException.class)
    public ResponseEntity<UnifyResponse> handleHttpException(HttpServletRequest req, HttpException e) {
        //response
        String requestURI = req.getRequestURI();
        String method = req.getMethod();
        UnifyResponse response = new UnifyResponse(e.getCode(), codeConfiguration.getMessage(e.getCode()), this.request(method,requestURI));
        //contentType
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        //httpStatus
        HttpStatus httpStatus = HttpStatus.resolve(e.getHttpStatusCode());
        //responseEntity
        ResponseEntity<UnifyResponse> responseEntity = new ResponseEntity<>(response, headers, httpStatus);
        return responseEntity;
    }

    //方法体参数异常
    @ExceptionHandler(MethodArgumentNotValidException.class)
    @ResponseBody
    @ResponseStatus(code = HttpStatus.BAD_REQUEST)
    public UnifyResponse handleBeanValidation(HttpServletRequest req, MethodArgumentNotValidException e) {
        String requestURI = req.getRequestURI();
        String method = req.getMethod();
        List<ObjectError> errors = e.getBindingResult().getAllErrors();
        String messages = this.formatAllErrorMessages(errors);
        return new UnifyResponse(10001,messages,this.request(method,requestURI));
    }

    //URL参数异常
    @ExceptionHandler(ConstraintViolationException.class)
    @ResponseBody
    @ResponseStatus(code = HttpStatus.BAD_REQUEST)
    public UnifyResponse handelException(HttpServletRequest req, ConstraintViolationException e){
        String requestURI = req.getRequestURI();
        String method = req.getMethod();
        StringBuffer errorMsg = new StringBuffer();
        for(ConstraintViolation error: e.getConstraintViolations()){
            String msg = error.getMessage();
            String m = error.getPropertyPath().toString();
            String name = m.split("[.]")[1];
            errorMsg.append(name).append(" ").append(msg).append(";");
        }
        String messages = errorMsg.toString();
        return new UnifyResponse(10001,messages,this.request(method,requestURI));
    }

    private String request(String method, String requestURI){
        return method + " " + requestURI;
    }

    private String formatAllErrorMessages(List<ObjectError> errors) {
        StringBuffer errorMsg = new StringBuffer();
        errors.forEach(error -> errorMsg.append(error.getDefaultMessage()).append(";"));
        return errorMsg.toString();
    }
}
