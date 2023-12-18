package com.example.springboot.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.time.LocalDate;
import java.util.List;

/***
 * @description:
 * @author: yk
 **/
@Data
public class Category {
    private Integer id;
    private String name;
    private String remark;
    private Integer pid;
    /***
     @JsonFormat(pattern = "yyyy-MM-dd",timezone = "GMT+8")
     目的是为了给前端页面展示标准的时间格式
     timezone = "GMT+8") 指定时区为东八区
    */
    @JsonFormat(pattern = "yyyy-MM-dd",timezone = "GMT+8")
    private LocalDate createtime;

    @JsonFormat(pattern = "yyyy-MM-dd",timezone = "GMT+8")
    private LocalDate updatetime;

    // children字段
    private List<Category> children;
}
