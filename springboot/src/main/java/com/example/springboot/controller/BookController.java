package com.example.springboot.controller;


import cn.hutool.core.io.FileUtil;
import cn.hutool.core.util.StrUtil;
import com.example.springboot.common.Result;
import com.example.springboot.controller.request.BookPageRequest;
import com.example.springboot.entity.Admin;
import com.example.springboot.entity.Book;
import com.example.springboot.service.IBookService;
import com.example.springboot.util.TokenUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.io.OutputStream;
import java.net.URLEncoder;
import java.util.List;

/***
 * @description: 展示数据 将数据返回给前端展示json 通过注入的方式与UserService关联
 * @author: yk
 **/
@CrossOrigin
@RestController
@Slf4j
@RequestMapping("/book")
public class BookController {

    @Autowired
    private IBookService bookService;

    private static final String BASE_FILE_PATH = System.getProperty("user.dir") + "/files/";

    //文件上传
    @PostMapping("/file/upload")
    public Result uploadFile(MultipartFile file) {
        //原始文件名称
        String originalFilename = file.getOriginalFilename();
        //上传的文件为空
        if (StrUtil.isBlank(originalFilename)) {
            return Result.error("文件上传失败");
        }
        //获取当前系统时间的时间戳
        long flag = System.currentTimeMillis();
        //文件路径
        String filePath = BASE_FILE_PATH + flag + "_" + originalFilename;
        try {
            //创建父级目录
            FileUtil.mkParentDirs(filePath);
            file.transferTo(FileUtil.file(filePath));
            //获取当前的用户信息(采用获取token)
            Admin currentAdmin = TokenUtils.getCurrentAdmin();
            //获取当前用户的id 输入密码的 用户信息的token
            String token = TokenUtils.genToken(currentAdmin.getId().toString(), currentAdmin.getPassword(), 15);
            String url = "http://localhost:8080/api/book/file/download/" + flag + "?&token=" + token;
            if (originalFilename.endsWith("png") || originalFilename.endsWith("jpg") || originalFilename.endsWith("pdf")) {
                url += "&play=1";
            }
            return Result.success(url);
            //文件下载
//            return Result.success("http://localhost:8080/api/book/file/download/" + flag + "?token=" + token);
            //文件预览
//            return Result.success("http://localhost:8080/api/book/file/download/" + flag + "?play=1&token=" + token);
        } catch (Exception e) {
            log.info("文件上传失败", e);
        }
        return Result.error("文件上传失败");
    }

    // System.currentTimeMillis()用于获取当前系统时间，以毫秒为单位
    //文件下载
    @GetMapping("/file/download/{flag}")
    public void download(@PathVariable String flag, @RequestParam(required = false) String play, HttpServletResponse response) {
        OutputStream os;
        List<String> fileNames = FileUtil.listFileNames(BASE_FILE_PATH);
        //相当于 System.currentTimeMillis() + originalFilename
        String fileName = fileNames.stream().filter(name -> name.contains(flag)).findAny().orElse("");
        try {
            if (StrUtil.isNotEmpty(fileName)) {
                //根据下划线进行截取
                String realName = fileName.substring(fileName.indexOf("_") + 1);
                if ("1".equals(play)) {
                    //头部进行设置第一种：预览下载
                    response.addHeader("Content-Disposition", "inline;filename=" + URLEncoder.encode(realName, "UTF-8"));
                } else {
                    ///头部进行设置第二种：附件下载
                    response.addHeader("Content-Disposition", "attachment;filename=" + URLEncoder.encode(realName, "UTF-8"));
                }
                //读取文件的字节流
                byte[] bytes = FileUtil.readBytes(BASE_FILE_PATH + fileName);
                //通过 os 响应出来
                os = response.getOutputStream();
                os.write(bytes);
                os.flush();
                os.close();
            }
        } catch (Exception e) {
            log.error("文件下载失败", e);
        }
    }


    /*
        @RequestBody主要用来接收前端传递给后端的json字符串中的数据的(请求体中的数据的),
        将User转换为user对象
        新增操作
    */
    @PostMapping("save")
    public Result save(@RequestBody Book obj) {
        bookService.save(obj);
        return Result.success();
    }

    /**
     * put请求是从客户端向服务端发送数据，但与post不同的是，
     * put真正的目的其实是执行大规模的替换操作，而不是更新操作。
     * <p>
     * 对数据的编辑修改操作
     */
    @PutMapping("/update")
    public Result update(@RequestBody Book obj) {
        bookService.update(obj);
        return Result.success();
    }

    //删除操作
    @DeleteMapping("/delete/{id}")
    public Result delete(@PathVariable Integer id) {
        bookService.deleteById(id);
        return Result.success();
    }

    //根据id来获取数据 是用于编辑按钮
    @GetMapping("{id}")
    public Result getById(@PathVariable Integer id) {
        //获取一条数据，不用list<Book>
        Book obj = bookService.getById(id);
        return Result.success(obj);
    }

    //查询操作
    @GetMapping("list")
    public Result list() {
//        List<Book> users = bookService.list();
        return Result.success(bookService.list());
    }

    //实现分页模糊查询
    @GetMapping("page")
    public Result page(BookPageRequest pageRequest) {
        return Result.success(bookService.page(pageRequest));
    }
}

