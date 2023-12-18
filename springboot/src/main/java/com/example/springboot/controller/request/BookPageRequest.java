package com.example.springboot.controller.request;

import lombok.Data;

/***
 * @description: 根据 用户名 标准码去查询
 * @author: yk
 **/
@Data
public class BookPageRequest extends BaseRequest {
  private String name;
  private String bookNo;
}
