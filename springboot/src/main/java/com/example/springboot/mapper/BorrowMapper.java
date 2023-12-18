package com.example.springboot.mapper;

import com.example.springboot.controller.request.BaseRequest;
import com.example.springboot.entity.Borrow;
import com.example.springboot.entity.Retur;
import com.example.springboot.mapper.po.BorrowReturCountPO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/***
 * @description: 对于数据库的绑定
 * @author: yk
 **/
@Mapper
public interface BorrowMapper {
    List<Borrow> list();

    //分页模糊查询 BaseRequest baseRequest利用多态来处理
    List<Borrow> listByCondition(BaseRequest baseRequest);

    List<Retur> listReturByCondition(BaseRequest baseRequest);

    //插入数据
    void save(Borrow obj);

    void saveRetur(Retur obj);

    //获取一条数据
    Borrow getById(Integer id);

    //对数据进行编辑修改操作
    void updateById(Borrow obj);

    void deleteById(Integer id);

    void deletReturById(Integer id);

    void updateStatus(String status, Integer id);

    List<BorrowReturCountPO> getCountByTimeRange(@Param("timeRange") String timeRange, @Param("type") int type);  // 1 borrow  2 return
}
