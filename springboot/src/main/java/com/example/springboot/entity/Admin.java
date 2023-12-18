package com.example.springboot.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.util.Date;

/***
 * @description:
 * @author: yk
 **/
@Data
public class Admin {
    private Integer id;
    private String username;
    private String phone;
    private String email;
    private String password;
    /***
     @JsonFormat(pattern = "yyyy-MM-dd",timezone = "GMT+8")
     目的是为了给前端页面展示标准的时间格式
     timezone = "GMT+8") 指定时区为东八区
    */
    @JsonFormat(pattern = "yyyy-MM-dd",timezone = "GMT+8")
    private Date createtime;

    @JsonFormat(pattern = "yyyy-MM-dd",timezone = "GMT+8")
    private Date updatetime;
    private boolean status;
}
