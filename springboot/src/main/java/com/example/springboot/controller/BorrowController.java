package com.example.springboot.controller;

import com.example.springboot.common.Result;
import com.example.springboot.controller.request.BorrowPageRequest;
import com.example.springboot.entity.Borrow;
import com.example.springboot.entity.Retur;
import com.example.springboot.service.Impl.BorrowService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/***
 * @description: 展示数据 将数据返回给前端展示json 通过注入的方式与UserService关联
 * @author: yk
 **/
//@CrossOrigin
@RestController
@RequestMapping("/borrow")
public class BorrowController {

    @Autowired
    private BorrowService borrowService;
   /*
       @RequestBody主要用来接收前端传递给后端的json字符串中的数据的(请求体中的数据的),
       将User转换为user对象
       新增操作
   */
    @PostMapping("save")
    public Result save(@RequestBody Borrow obj){
        borrowService.save(obj);
        return Result.success();
    }
    /**
     put请求是从客户端向服务端发送数据，但与post不同的是，
     put真正的目的其实是执行大规模的替换操作，而不是更新操作。

     对数据的编辑修改操作
    */
    @PutMapping("/update")
    public Result update(@RequestBody Borrow obj){
        borrowService.update(obj);
        return Result.success();
    }

    //删除操作
    @DeleteMapping("/delete/{id}")
    public Result delete(@PathVariable Integer id){
        borrowService.deleteById(id);
        return Result.success();
    }

    //根据id来获取数据 是用于编辑按钮
    @GetMapping("{id}")
    public Result getById(@PathVariable Integer id){
        //获取一条数据，不用list<Borrow>
        Borrow obj = borrowService.getById(id);
        return Result.success(obj);
    }
    //查询操作
    @GetMapping("list")
    public Result list(){
//        List<Borrow> users = borrowService.list();
        return Result.success(borrowService.list());
    }
    //实现分页模糊查询
    @GetMapping("page")
    public Result page(BorrowPageRequest pageRequest){
        return Result.success(borrowService.page(pageRequest));
    }

    @GetMapping("pageRetur")
    public Result pageRetur(BorrowPageRequest pageRequest){
        return Result.success(borrowService.pageRetur(pageRequest));
    }
    @PostMapping("saveRetur")
    public Result saveRetur(@RequestBody Retur obj){
        borrowService.saveRetur(obj);
        return Result.success();
    }
    @DeleteMapping("/deleteRetur/{id}")
    public Result deleteRetur(@PathVariable  Integer id){
        borrowService.deleteReturById(id);
        return Result.success();
    }

    //timeRange: week moth moth2 month3
    @GetMapping("/lineCharts/{timeRange}")
    public Result lineCharts(@PathVariable String timeRange) {
        return Result.success(borrowService.getCountByTimeRange(timeRange));
    }
}

