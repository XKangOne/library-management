package com.example.springboot.service;

import com.example.springboot.controller.dto.LoginDTO;
import com.example.springboot.controller.request.BaseRequest;
import com.example.springboot.controller.request.LoginRequest;
import com.example.springboot.controller.request.PasswordRequest;
import com.example.springboot.entity.Book;
import com.example.springboot.entity.Book;
import com.github.pagehelper.PageInfo;

import java.util.List;

/***
 * @description: 实现数据库的接口
 * @author: yk
 **/
public interface IBookService {
    List<Book> list();

   PageInfo<Book> page(BaseRequest baseRequest);

    void save(Book obj);

    Book getById(Integer id);

    void update(Book obj);

    void deleteById(Integer id);
}
