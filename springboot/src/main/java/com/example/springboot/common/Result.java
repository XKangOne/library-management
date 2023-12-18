package com.example.springboot.common;

import lombok.Data;

/***
 * @description: 通过Result来包装后台数据,返回给前端
 * @author: yk
 **/

@Data
public class Result {
    private static final String SUCCESS_CODE = "200";
    private static final String ERROR_CODE = "-1";
    //响应结果
    private String code;
    //响应数据
    private Object data;
    //存放错误的信息
    private String msg;

    //返回成功的信息
    public static Result success(){
        Result result = new Result();
        result.setCode(SUCCESS_CODE);
        return result;
    }
    //返回成功的有参信息
    public static Result success(Object data){
        Result result = new Result();
        result.setCode(SUCCESS_CODE);
        result.setData(data);
        return result;
    }
    //返回错误的信息
    public static Result error(String msg){
        Result result = new Result();
        result.setCode(ERROR_CODE);
        result.setMsg(msg);
        return result;
    }
    public static Result error(String code,String msg){
        Result result = new Result();
        result.setCode(code);
        result.setMsg(msg);
        return result;
    }
}
