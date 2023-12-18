package com.example.springboot.controller.request;

import lombok.Data;

/***
 * @description: 目的是方便其他类继承 因为是通用的 分页页数 每页共10个数
 * @author: yk
 **/
@Data
public class BaseRequest {
    //页码数量 存入默认值 防止报错
    private Integer pageNum = 1;
    //页码大小 存入默认值 防止报错
    private Integer pageSize = 10;
}
