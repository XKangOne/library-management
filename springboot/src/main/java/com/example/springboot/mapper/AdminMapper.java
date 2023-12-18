package com.example.springboot.mapper;

import com.example.springboot.controller.request.BaseRequest;
import com.example.springboot.controller.request.LoginRequest;
import com.example.springboot.controller.request.PasswordRequest;
import com.example.springboot.entity.Admin;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

/***
 * @description: 对于数据库的绑定
 * @author: yk
 **/
@Mapper
public interface AdminMapper {
    List<Admin> list();

    //分页模糊查询 BaseRequest baseRequest利用多态来处理
   List<Admin> listByCondition (BaseRequest baseRequest);

   //插入数据
    void save(Admin admin);

    //获取一条数据
    Admin getById(Integer id);

    //对数据进行编辑修改操作
    void updateById(Admin admin);

    void deleteById(Integer id);

    //获取用户名和密码
    //使用@Param 就相当于给了两个参数 不用在LoginRequest中设置无参和有参构造函数
//    Admin getByUsernameAndPassword(@Param("username")String username,@Param("password") String password);
    Admin getByUsernameAndPassword(LoginRequest request);

    //更新密码
//    int updatePassword(@Param("newPass") String newPass,@Param("username") String username);
    int updatePassword(PasswordRequest request);

    //查询用户名 为了重复管理员无法创建的目的
    Admin getByUsername(String username);
}
