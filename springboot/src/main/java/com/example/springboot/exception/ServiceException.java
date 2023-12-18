package com.example.springboot.exception;

/***
    自定义异常把错误的信息暴露出去
    处理业务时的异常
 **/
public class ServiceException extends RuntimeException{

    private String code;

    public String getCode() {
        return code;
    }
    public ServiceException(String message,Throwable cause){
        super(message,cause);
    }
    public ServiceException(String message){
        super(message);
    }

    public ServiceException(String code,String message){
        super(message);
        this.code = code;
    }
}
