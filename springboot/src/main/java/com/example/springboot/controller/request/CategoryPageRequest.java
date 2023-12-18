package com.example.springboot.controller.request;

import lombok.Data;

/***
 * @description: 根据 用户名 去查询
 * @author: yk
 **/
@Data
public class CategoryPageRequest extends BaseRequest {
  private String name;
}
