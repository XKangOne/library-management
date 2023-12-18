package com.example.springboot.service.Impl;

import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.IdUtil;
import com.example.springboot.controller.request.UserPageRequest;
import com.example.springboot.entity.User;
import com.example.springboot.mapper.UserMapper;
import com.example.springboot.service.IUserService;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.Date;
import java.util.List;

/***
 * @description: 业务层  实现数据库接口，通过注入的方式与UserMapper关联,主要是写业务逻辑
 * @author: yk
 **/
@Service
public class UserService implements IUserService {
    @Resource
    private UserMapper userMapper;
    @Override
    public List<User> list() {
        return userMapper.list();
    }

    @Override
    public PageInfo<User> page(UserPageRequest userPageRequest) {
        //根据PageHelper将pageNum和pageSize传入到userPageRequest,数据是从前端传过来的
        PageHelper.startPage(userPageRequest.getPageNum(),userPageRequest.getPageSize());
        List<User> users = userMapper.listByCondition(userPageRequest);
        //返回给前台
        return new PageInfo<>(users);
    }
    @Override
    public void save(User user) {
        //将username当做卡号来处理 加入是为了DateUtil.format()形成 日期+编号
        //Math.abs(IdUtil.fastSimpleUUID().hashCode())将hashcode值变成正数
        Date date = new Date();
        user.setUsername(DateUtil.format(date,"yyyyMMdd")+Math.abs(IdUtil.fastSimpleUUID().hashCode()));
        userMapper.save(user);
    }

    @Override
    public User getById(Integer id) {
        return userMapper.getById(id);
    }

    @Override
    public void update(User user) {
        //修改了数据更新修改时间的操作
        user.setUpdatetime(new Date());
        userMapper.updateById(user);
    }

    @Override
    public void deleteById(Integer id) {
        userMapper.deleteById(id);
    }

    @Override
    public void handleAccount(User user) {
        //点击充值按钮 获取其中输入的score
        Integer score = user.getScore();
        if (score == null) {
            return;
        }
        //从数据库将id 信息传递过来
        Integer id = user.getId();
        //获取id的数据
        User dbuser = userMapper.getById(id);
        //为了更新本身的account再加上输入的score 和的信息
        dbuser.setAccount(dbuser.getAccount() + score);
        userMapper.updateById(dbuser);
    }
}
