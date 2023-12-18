package com.example.springboot.controller.dto;

import lombok.Data;

/***
 * @description: 返回登录的基本信息
 * @author: yk
 **/
@Data
public class LoginDTO {
    private Integer id;
    private String username;
    private String phone;
    private String email;
    private String token;
}
