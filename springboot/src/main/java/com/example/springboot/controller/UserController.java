package com.example.springboot.controller;

import com.example.springboot.common.Result;
import com.example.springboot.controller.request.UserPageRequest;
import com.example.springboot.entity.User;
import com.example.springboot.service.Impl.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/***
 * @description: 展示数据 将数据返回给前端展示json 通过注入的方式与UserService关联
 * @author: yk
 **/
@CrossOrigin
@RestController
@RequestMapping("/user")
public class UserController {

    @Autowired
    private UserService userService;

    @PostMapping("/account")
    public Result account(@RequestBody User user) {
        userService.handleAccount(user);
        return Result.success();
    }

   /*
       @RequestBody主要用来接收前端传递给后端的json字符串中的数据的(请求体中的数据的),
       将User转换为user对象
       新增操作
   */
    @PostMapping("save")
    public Result save(@RequestBody User user){
        userService.save(user);
        return Result.success();
    }

    /**
     put请求是从客户端向服务端发送数据，但与post不同的是，
     put真正的目的其实是执行大规模的替换操作，而不是更新操作。

     对数据的编辑修改操作
    */
    @PutMapping("/update")
    public Result update(@RequestBody User user){
        userService.update(user);
        return Result.success();
    }

    //删除操作
    @DeleteMapping("/delete/{id}")
    public Result delete(@PathVariable Integer id){
        userService.deleteById(id);
        return Result.success();
    }

    //根据id来获取数据 是用于编辑按钮
    @GetMapping("{id}")
    public Result getById(@PathVariable Integer id){
        //获取一条数据，不用list<User>
        User users = userService.getById(id);
        return Result.success(users);
    }
    //查询操作
    @GetMapping("/list")
    public Result list(){
//        List<User> users = userService.list();
        return Result.success(userService.list());
    }

    //实现分页模糊查询
    @GetMapping("page")
    public Result page(UserPageRequest userPageRequest){
        return Result.success(userService.page(userPageRequest));
    }
}
