package com.example.springboot.controller.request;

import lombok.Data;

/***
 * @description: 根据name 和 phone 去查询
 * @author: yk
 **/
@Data
public class UserPageRequest extends BaseRequest {
    private String name;
    private String phone;
}
