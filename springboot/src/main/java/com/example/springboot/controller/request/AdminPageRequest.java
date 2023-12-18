package com.example.springboot.controller.request;

import lombok.Data;

/***
 * @description: 根据用户名 手机号 邮箱去 查询
 * @author: yk
 **/
@Data
public class AdminPageRequest extends BaseRequest {
    private String username;
    private String phone;
    private String email;
}
