package com.example.springboot.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

/***
 * @description:
 * @author: yk
 **/
@Data
@AllArgsConstructor
@NoArgsConstructor
public class User {
    private Integer id;
    private String name;
    private String username;
    private Integer age;
    private Integer score;
    private String sex;
    private String phone;
    private String address;
    private Integer account;

    /***
     @JsonFormat(pattern = "yyyy-MM-dd",timezone = "GMT+8")
     目的是为了给前端页面展示标准的时间格式

     timezone = "GMT+8") 指定时区为东八区
    */
    @JsonFormat(pattern = "yyyy-MM-dd",timezone = "GMT+8")
    private Date createtime;

    @JsonFormat(pattern = "yyyy-MM-dd",timezone = "GMT+8")
    private Date updatetime;

    //状态
    private boolean status;
}
