package com.example.springboot.controller.request;

import lombok.Data;

/***
 修改密码的请求参数
 **/

@Data
public class PasswordRequest {
    private String username;
    private String password;
    private String newPass;
}
