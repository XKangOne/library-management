package com.example.springboot.mapper;

import com.example.springboot.controller.request.BaseRequest;
import com.example.springboot.controller.request.LoginRequest;
import com.example.springboot.controller.request.PasswordRequest;
import com.example.springboot.entity.Book;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

/***
 * @description: 对于数据库的绑定
 * @author: yk
 **/
@Mapper
public interface BookMapper {
    List<Book> list();

    //分页模糊查询 BaseRequest baseRequest利用多态来处理
   List<Book> listByCondition (BaseRequest baseRequest);

   //插入数据
    void save(Book obj);

    //获取一条数据
    Book getById(Integer id);

    //对数据进行编辑修改操作
    void updateById(Book obj);

    void deleteById(Integer id);

    //获取图书标准码
    Book getByNo(String bookNo);

    void updateNumByNo(String bookNo);
}
