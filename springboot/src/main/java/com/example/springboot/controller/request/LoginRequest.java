package com.example.springboot.controller.request;

import lombok.Data;

/***
 * @description: 输入账号和密码
 * @author: yk
 **/
@Data
public class LoginRequest {
    private String username;
    private String password;
}
