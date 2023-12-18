package com.example.springboot.exception;

import cn.hutool.core.util.StrUtil;
import com.example.springboot.common.Result;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

/***
 * @description: 全局统一异常处理
 * @author: yk
 **/
@Slf4j
@RestControllerAdvice
public class ExceptionHandle {
    @ExceptionHandler(value = ServiceException.class)
    public Result serviceExceptionError(ServiceException e){
        log.error("业务异常",e);
        String code = e.getCode();
        //如果有code
        if (StrUtil.isNotBlank(code))
        {
            //返回code码 和信息
            return Result.error(code, e.getMessage());
        }
        //否则就返回信息
        return Result.error(e.getMessage());
    }

    //没有捕获的异常处理的错误，统一的称为系统错误(可以指Sql语言输入错误，与业务，登录账号密码无关)
    @ExceptionHandler(value = Exception.class)
    public Result exceptionError(Exception e){
        log.error("系统错误",e);
        return Result.error("系统错误");
    }
}
