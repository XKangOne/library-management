package com.example.springboot.service;

import com.example.springboot.controller.dto.LoginDTO;
import com.example.springboot.controller.request.*;
import com.example.springboot.entity.Admin;
import com.example.springboot.entity.User;
import com.github.pagehelper.PageInfo;

import java.util.List;

/***
 * @description: 实现数据库的接口
 * @author: yk
 **/
public interface IAdminService {
    List<Admin> list();

   PageInfo<Admin> page(BaseRequest baseRequest);

    void save(Admin obj);

    Admin getById(Integer id);

    void update(Admin obj);

    void deleteById(Integer id);

    LoginDTO login(LoginRequest request);

    //修改密码
    void changePass(PasswordRequest request);
}
