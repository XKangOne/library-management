package com.example.springboot.controller;

import cn.hutool.core.collection.CollUtil;
import com.example.springboot.common.Result;
import com.example.springboot.controller.request.AdminPageRequest;
import com.example.springboot.controller.request.CategoryPageRequest;
import com.example.springboot.entity.Category;
import com.example.springboot.service.Impl.CategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

/***
 * @description: 展示数据 将数据返回给前端展示json 通过注入的方式与UserService关联
 * @author: yk
 **/
//@CrossOrigin
@RestController
@RequestMapping("/category")
public class CategoryController {

    @Autowired
    private CategoryService categoryService;
    
   /*
       @RequestBody主要用来接收前端传递给后端的json字符串中的数据的(请求体中的数据的),
       将User转换为user对象
       新增操作
   */
    @PostMapping("save")
    public Result save(@RequestBody Category obj){
        categoryService.save(obj);
        return Result.success();
    }
    /**
     put请求是从客户端向服务端发送数据，但与post不同的是，
     put真正的目的其实是执行大规模的替换操作，而不是更新操作。

     对数据的编辑修改操作
    */
    @PutMapping("/update")
    public Result update(@RequestBody Category obj){
        categoryService.update(obj);
        return Result.success();
    }

    //删除操作
    @DeleteMapping("/delete/{id}")
    public Result delete(@PathVariable Integer id){
        categoryService.deleteById(id);
        return Result.success();
    }

    //根据id来获取数据 是用于编辑按钮
    @GetMapping("{id}")
    public Result getById(@PathVariable Integer id){
        //获取一条数据，不用list<Category>
        Category obj = categoryService.getById(id);
        return Result.success(obj);
    }
    //查询操作
    @GetMapping("list")
    public Result list(){
//        List<Category> users = categoryService.list();
        return Result.success(categoryService.list());
    }

    //实现分页模糊查询
    @GetMapping("page")
    public Result page(CategoryPageRequest pageRequest){
        return Result.success(categoryService.page(pageRequest));
    }
    @GetMapping("/tree")
    public Result tree(){
        List<Category> list = categoryService.list();

        //对list操作 过滤保留第一层数据
//        List<Category> treelist = list.stream().filter(v->v.getPid()==null).collect(Collectors.toList());

        //比如 递归实现 children 列表的查询
        return Result.success(createTree(null,list)); //null表示第一级开始递归
    }

    //完成递归的方法来实现递归树
    private List<Category> createTree(Integer pid, List<Category> categories) {
        List<Category> treeList = new ArrayList<>();

        for (Category category : categories){
            if (pid == null) {
                if (category.getPid() == null) {  //那这就是第一级节点
                    treeList.add(category);
                    category.setChildren(createTree(category.getId(),categories));
                }
            }else {
                if (pid.equals(category.getPid())) {
                    treeList.add(category);
                    category.setChildren(createTree(category.getId(),categories));
                }
            }
            //当子集children没有值 则为null 不显示
            if (CollUtil.isEmpty(category.getChildren())) {
                category.setChildren(null);
            }
        }
        return treeList;
    }
}
