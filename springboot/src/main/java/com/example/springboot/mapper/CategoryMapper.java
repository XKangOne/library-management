package com.example.springboot.mapper;

import com.example.springboot.controller.request.BaseRequest;
import com.example.springboot.controller.request.LoginRequest;
import com.example.springboot.controller.request.PasswordRequest;
import com.example.springboot.entity.Admin;
import com.example.springboot.entity.Category;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

/***
 * @description: 对于数据库的绑定
 * @author: yk
 **/
@Mapper
public interface CategoryMapper {
    List<Category> list();

    //分页模糊查询 BaseRequest baseRequest利用多态来处理
   List<Category> listByCondition (BaseRequest baseRequest);

   //插入数据
    void save(Category obj);

    //获取一条数据
    Category getById(Integer id);

    //对数据进行编辑修改操作
    void updateById(Category obj);

    void deleteById(Integer id);

}
