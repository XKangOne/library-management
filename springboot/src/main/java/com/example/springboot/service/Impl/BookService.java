package com.example.springboot.service.Impl;

import cn.hutool.core.collection.CollUtil;
import com.example.springboot.controller.request.BaseRequest;
import com.example.springboot.entity.Book;
import com.example.springboot.exception.ServiceException;
import com.example.springboot.mapper.BookMapper;
import com.example.springboot.service.IBookService;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.time.LocalDate;
import java.util.List;

@Service
@Slf4j
public class BookService implements IBookService {
    @Resource
    private BookMapper bookMapper;

    @Override
    public List<Book> list() {
        return bookMapper.list();
    }

    @Override
    public PageInfo<Book> page(BaseRequest baseRequest) {
        //根据PageHelper将pageNum和pageSize传入到userPageRequest,数据是从前端传过来的
        PageHelper.startPage(baseRequest.getPageNum(), baseRequest.getPageSize());
        //自关联查询
//        List<Book> books = bookMapper.listByCondition(baseRequest);
        //返回给前台
        return new PageInfo<>(bookMapper.listByCondition(baseRequest));
    }

    @Override
    public void save(Book obj) {
        try {
            //图书管理中分类插入保存的操作
            obj.setCategory(category(obj.getCategories()));
            bookMapper.save(obj);
        } catch (Exception e) {
            throw new ServiceException("数据插入错误", e);
        }
    }
    @Override
    public Book getById(Integer id) {
        return bookMapper.getById(id);
    }

    @Override
    public void update(Book obj) {
        try {
            //更新图书管理分类编辑的操作
            obj.setCategory(category(obj.getCategories()));
            //修改了数据更新修改时间的操作
            obj.setUpdatetime(LocalDate.now());
            bookMapper.updateById(obj);
        } catch (Exception e) {
            throw new ServiceException("数据更新错误", e);
        }
    }
    @Override
    public void deleteById(Integer id) {
        bookMapper.deleteById(id);
    }

    private String category(List<String> categories) {
        StringBuilder builder = new StringBuilder();
        //如果该数组不为空
        if (CollUtil.isNotEmpty(categories)) {
            //进行遍历追加每一个数据后面加一个  >
            categories.forEach(v -> builder.append(v).append(">"));
            //返回去除最后一个 >
            return builder.substring(0, builder.lastIndexOf(">"));
        }
        return builder.toString();
    }
}
