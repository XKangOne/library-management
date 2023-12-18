package com.example.springboot.mapper;

import com.example.springboot.controller.request.UserPageRequest;
import com.example.springboot.entity.User;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/***
 * @description: 对于数据库的绑定
 * @author: yk
 **/
@Mapper
public interface UserMapper {

    //查询所有数据
//    @Select("select * from user")
    List<User> list();

    //分页模糊查询
   List<User> listByCondition (UserPageRequest userPageRequest);

   //插入数据
    void save(User user);

    //获取一条数据
    User getById(Integer id);

    //对数据进行编辑修改操作
    void updateById(User user);

    void deleteById(Integer id);

    //获取借书的用户会员码
    User getByUsername(String userNo);
//
//    void updateByUserNo(@Param("score") int score,@Param("userNo") String userNo);
}
