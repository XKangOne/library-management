package com.example.springboot.controller;

import com.example.springboot.common.Result;
import com.example.springboot.controller.dto.LoginDTO;
import com.example.springboot.controller.request.AdminPageRequest;
import com.example.springboot.controller.request.LoginRequest;
import com.example.springboot.controller.request.PasswordRequest;
import com.example.springboot.controller.request.UserPageRequest;
import com.example.springboot.entity.Admin;
import com.example.springboot.service.Impl.AdminService;
import com.example.springboot.service.Impl.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/***
 * @description: 展示数据 将数据返回给前端展示json 通过注入的方式与UserService关联
 * @author: yk
 **/
//@CrossOrigin
@RestController
@RequestMapping("/admin")
public class AdminController {

    @Autowired
    private AdminService adminService;

    @PostMapping("/login")
    public Result login(@RequestBody LoginRequest request){
        LoginDTO login = adminService.login(request);
        return Result.success(login);
    }

    //修改密码
    @PutMapping("/password")
    public Result Password(@RequestBody PasswordRequest request){
        //修改密码没有返回对象
        adminService.changePass(request);
        return Result.success();
    }
   /*
       @RequestBody主要用来接收前端传递给后端的json字符串中的数据的(请求体中的数据的),
       将User转换为user对象
       新增操作
   */
    @PostMapping("save")
    public Result save(@RequestBody Admin obj){
        adminService.save(obj);
        return Result.success();
    }
    /**
     put请求是从客户端向服务端发送数据，但与post不同的是，
     put真正的目的其实是执行大规模的替换操作，而不是更新操作。

     对数据的编辑修改操作
    */
    @PutMapping("/update")
    public Result update(@RequestBody Admin obj){
        adminService.update(obj);
        return Result.success();
    }

    //删除操作
    @DeleteMapping("/delete/{id}")
    public Result delete(@PathVariable Integer id){
        adminService.deleteById(id);
        return Result.success();
    }

    //根据id来获取数据 是用于编辑按钮
    @GetMapping("{id}")
    public Result getById(@PathVariable Integer id){
        //获取一条数据，不用list<Admin>
        Admin obj = adminService.getById(id);
        return Result.success(obj);
    }
    //查询操作
    @GetMapping("list")
    public Result list(){
//        List<Admin> users = adminService.list();
        return Result.success(adminService.list());
    }

    //实现分页模糊查询
    @GetMapping("page")
    public Result page(AdminPageRequest pageRequest){
        return Result.success(adminService.page(pageRequest));
    }
}
