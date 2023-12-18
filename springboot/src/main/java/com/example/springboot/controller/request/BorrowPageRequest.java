package com.example.springboot.controller.request;

import lombok.Data;

/***
 * @description:
 * @author: yk
 **/
@Data
public class BorrowPageRequest extends BaseRequest{
    private String bookName;
    private String bookNo;
    private String userName;
}
