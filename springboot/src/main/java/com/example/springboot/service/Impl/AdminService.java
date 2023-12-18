package com.example.springboot.service.Impl;

import cn.hutool.core.util.StrUtil;
import cn.hutool.crypto.SecureUtil;
import com.example.springboot.controller.dto.LoginDTO;
import com.example.springboot.controller.request.BaseRequest;
import com.example.springboot.controller.request.LoginRequest;
import com.example.springboot.controller.request.PasswordRequest;
import com.example.springboot.entity.Admin;
import com.example.springboot.exception.ServiceException;
import com.example.springboot.mapper.AdminMapper;
import com.example.springboot.service.IAdminService;
import com.example.springboot.util.TokenUtils;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.Date;
import java.util.List;

/***
 * @description: 业务层  实现数据库接口，通过注入的方式与adminMapper关联,主要是写业务逻辑
 * @author: yk
 **/
@Service
@Slf4j
public class AdminService implements IAdminService {
    @Resource
    private AdminMapper adminMapper;

    //设置默认密码
    private static final String DEFAULT_PASS = "123";

    //加盐 填写了一个固定字符串 无作用
    private static final String PASS_SALT = "qingge";
    @Override
    public List<Admin> list() {
        return adminMapper.list();
    }

    @Override
    public PageInfo<Admin> page(BaseRequest baseRequest) {
        //根据PageHelper将pageNum和pageSize传入到userPageRequest,数据是从前端传过来的
        PageHelper.startPage(baseRequest.getPageNum(),baseRequest.getPageSize());
        List<Admin> admins = adminMapper.listByCondition(baseRequest);
        //返回给前台
        return new PageInfo<>(admins);
    }
    @Override
    public void save(Admin obj) {
        //如果设置的密码为空
        if (StrUtil.isBlank(obj.getPassword())){
            //则设置默认密码为123
            obj.setPassword(DEFAULT_PASS);
        }
        //给密码 设置md5加密，并加盐,借助SecureUtil工具包
//        obj.setPassword(SecureUtil.md5(obj.getPassword() + PASS_SALT));
        //调用下面封装好的
        obj.setPassword(securePass(obj.getPassword()));
        try{
            adminMapper.save(obj);
        }catch (Exception e){
            log.error("数据插入失败,username:{}",obj.getUsername(),e);
            throw new ServiceException("用户名重复");
        }
    }

    @Override
    public Admin getById(Integer id) {
        return adminMapper.getById(id);
    }

    @Override
    public void update(Admin obj) {
        //修改了数据更新修改时间的操作
        obj.setUpdatetime(new Date());
        adminMapper.updateById(obj);
    }

    @Override
    public void deleteById(Integer id) {
        adminMapper.deleteById(id);
    }

    @Override
    public LoginDTO login(LoginRequest request) {
        //进行加盐的解密 方便登录输入值可以识别清楚
//        request.setPassword(securePass(request.getPassword()));

        //如果用户名存在多个情况，使用try catch进行保护来抛出异常信息
        //request.getUsername() 使用request来请求username数据
        Admin admin = null;
        try {
            admin = adminMapper.getByUsername(request.getUsername());
        }catch (Exception e){
            log.error("根据用户名{} 查询出错",request.getUsername());
            throw new ServiceException("用户名错误");
        }
        //通过账号和密码来返回值
//        Admin admin = adminMapper.getByUsernameAndPassword(request);
        if (admin == null){
            throw new ServiceException("用户名或密码错误");
        }
        //判断密码是否合法，获取加密的密码返回给securePass对象
        String securePass = securePass(request.getPassword());
        if (!securePass.equals(admin.getPassword())) {
            throw new ServiceException("用户名或密码错误");
        }
        //登录时的禁用状态
        if (!admin.isStatus()){
            throw new ServiceException("该状态已经禁用无法登录！");
        }
        //通过BeanUtils 将 admin对象转换为LoginDTO
        LoginDTO loginDTO = new LoginDTO();
        BeanUtils.copyProperties(admin,loginDTO);

        //生成token
        String token = TokenUtils.genToken(String.valueOf(admin.getId()), admin.getPassword());
        loginDTO.setToken(token);
        return loginDTO;
    }

    //修改密码
    @Override
    public void changePass(PasswordRequest request) {
        //注意 你要对新的密码进行加密
        request.setNewPass(securePass(request.getNewPass()));
        //来判断数据库有没有更新成功的记录，有就给count 然后进行下面判断
        int count = adminMapper.updatePassword(request);
        //说明没有修改成功
        if (count <= 0){
            throw new ServiceException("修改密码失败");
        }
    }

    //对md5 加密加盐 进行封装,方便上面调用
    private String securePass(String password){
        return SecureUtil.md5(password + PASS_SALT);
    }
}
