package com.example.springboot.service.Impl;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.date.DateField;
import cn.hutool.core.date.DateTime;
import cn.hutool.core.date.DateUtil;
import com.example.springboot.controller.request.BaseRequest;
import com.example.springboot.entity.*;
import com.example.springboot.entity.Borrow;
import com.example.springboot.exception.ServiceException;
import com.example.springboot.mapper.BookMapper;
import com.example.springboot.mapper.BorrowMapper;
import com.example.springboot.mapper.UserMapper;
import com.example.springboot.mapper.po.BorrowReturCountPO;
import com.example.springboot.service.IBorrowService;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.*;

@Service
@Slf4j
public class BorrowService implements IBorrowService {
    @Resource
    private BorrowMapper borrowMapper;
    @Resource
    private UserMapper userMapper;
    @Resource
    private BookMapper bookMapper;

    @Override
    public List<Borrow> list() {
        return borrowMapper.list();
    }

    @Override
    public PageInfo<Borrow> page(BaseRequest baseRequest) {
        //根据PageHelper将pageNum和pageSize传入到userPageRequest,数据是从前端传过来的
        PageHelper.startPage(baseRequest.getPageNum(), baseRequest.getPageSize());
        //自关联查询
//        List<Borrow> books = borrowMapper.listByCondition(baseRequest);
        List<Borrow> borrows = borrowMapper.listByCondition(baseRequest);
        for (Borrow borrow : borrows) {
            LocalDate returnDate = borrow.getReturnDate();
            LocalDate now = LocalDate.now();
            //当前日期比归还的日期小一天
            if (now.plusDays(1).isEqual(returnDate)) {
                borrow.setNote("即将到期");
            } else if (now.isEqual(returnDate)) {
                borrow.setNote("已到期");
            } else if (now.isAfter(returnDate)) {
                borrow.setNote("已过期");
            } else {
                borrow.setNote("正常");
            }
        }
        //返回给前台
        return new PageInfo<>(borrows);
    }

    @Override
    public PageInfo<Retur> pageRetur(BaseRequest baseRequest) {
        PageHelper.startPage(baseRequest.getPageNum(), baseRequest.getPageSize());
        return new PageInfo<>(borrowMapper.listReturByCondition(baseRequest));
    }

    //还书的逻辑
    @Override
    @Transactional
    public void saveRetur(Retur obj) {
        //obj.getId()是前端传来的借书Id 更新时更改状态
        borrowMapper.updateStatus("已归还", obj.getId());
//        obj.setId(null); //新数据
        //实际归还日期
        obj.setRealDate(LocalDate.now());
        //除去更新时 改状态
        obj.setStatus("已归还");
        borrowMapper.saveRetur(obj);

        //图书数量增加
        bookMapper.updateNumByNo(obj.getBookNo());

        //返还和扣除用户积分
        //通过BookNo查出唯一的book 也就是这本书的信息 包括积分等等
        Book book = bookMapper.getByNo(obj.getBookNo());
        //如果这本书存在
        if (book != null) {
            long until = 0;
            //如果返还的日期在实际归还的日期之前
            if (obj.getRealDate().isBefore(obj.getReturnDate())) {
                //实际的日期与返还的日期相差多少天(也就是天数)
                until = obj.getRealDate().until(obj.getReturnDate(), ChronoUnit.DAYS);
            } else if (obj.getRealDate().isAfter(obj.getReturnDate())) { //逾期归还，要扣额外的积分
                until = -obj.getReturnDate().until(obj.getRealDate(), ChronoUnit.DAYS);
            }
            //获取待归还的积分(天数 * 积分)
            int score = (int) until * book.getScore();
            //得到用户的信息
            User user = userMapper.getByUsername(obj.getUserNo());
            //返回的余额加上积分
            int account = user.getAccount() + score;
            user.setAccount(account);
            if (account < 0) {
                //锁定账号
                user.setStatus(false);
            }
            //更新用户数据
            userMapper.updateById(user);
        }
    }

    @Override
    public void deleteReturById(Integer id) {
        borrowMapper.deletReturById(id);
    }

    @Override
    public Map<String, Object> getCountByTimeRange(String timeRange) {
        Map<String, Object> map = new HashMap<>();
        Date today = new Date();
        List<DateTime> dateRange;
        switch (timeRange) {
            case "week":
                // offsetDay 计算时间的一个工具方法
                // rangeToList 返回从开始时间到结束时间的一个时间范围
                dateRange = DateUtil.rangeToList(DateUtil.offsetDay(today, -6), today, DateField.DAY_OF_WEEK);
                break;
            case "month":
                dateRange = DateUtil.rangeToList(DateUtil.offsetDay(today, -29), today, DateField.DAY_OF_MONTH);
                break;
            case "month2":
                dateRange = DateUtil.rangeToList(DateUtil.offsetDay(today, -59), today, DateField.DAY_OF_MONTH);
                break;
            case "month3":
                dateRange = DateUtil.rangeToList(DateUtil.offsetDay(today, -89), today, DateField.DAY_OF_MONTH);
                break;
            default:
                dateRange = new ArrayList<>();
        }
        //  datetimeToDateStr() 就是一个处理的方法， 把 DateTime类型的List转换成一个 String的List
        List<String> dateStrRange = datetimeToDateStr(dateRange);
        map.put("date", dateStrRange);  // x轴的日期数据生产完毕

        //  timeRange = week  month 就是从前端传过来的
        //getCountByTimeRange 从数据库中查询出来的
        // getCountByTimeRange 不会统计数据库没有的日期，比如 数据库 11.4 这一天数据没有，他不会返回 date=2022-11-04,count=0 这个数据
        List<BorrowReturCountPO> borrowCount = borrowMapper.getCountByTimeRange(timeRange, 1);//type:1 是borrow  type:2 是return
        List<BorrowReturCountPO> returnCount = borrowMapper.getCountByTimeRange(timeRange, 2);
        map.put("borrow", countList(borrowCount, dateStrRange));
        map.put("retur", countList(returnCount, dateStrRange));
        return map;
    }

    // 把 DateTime类型的List转换成一个 String的List
    private List<String> datetimeToDateStr(List<DateTime> dateTimeList) {
        List<String> list = CollUtil.newArrayList();
        if (CollUtil.isEmpty(dateTimeList)) {
            return list;
        }
        //遍历
        for (DateTime dateTime : dateTimeList) {
            String date = DateUtil.formatDate(dateTime);
            list.add(date);
        }
        return list;
    }
    // 对数据库未统计的时间进行处理
    private List<Integer> countList(List<BorrowReturCountPO> countPOList, List<String> dateRange) {
        List<Integer> list = CollUtil.newArrayList();
        if (CollUtil.isEmpty(countPOList)) {
            return list;
        }
        //遍历 dataRange
        for (String date : dateRange) {
            // .map(BorrowReturCountPO::getCount) 取出 对象里的 count值
            // orElse(0) 对没匹配的数据(就是说没有的数据)返回0
            // "2022-11-04" 没有的话 就返回0
            Integer count = countPOList.stream().filter(countPO -> date.equals(countPO.getDate()))
                    .map(BorrowReturCountPO::getCount).findFirst().orElse(0);
            list.add(count);
        }
        // 最后返回的list的元素个数会跟 dateRange 的元素个数完全一样
        // dateRange: [
        //            "2022-10-30",
        //            "2022-10-31",
        //            "2022-11-01",
        //            "2022-11-02",
        //            "2022-11-03",
        //            "2022-11-04",
        //            "2022-11-05"
        //        ],
        // borrow: [
        //            0,
        //            0,
        //            2,
        //            1,
        //            0,
        //            1,
        //            3
        //        ]
        return list;
    }

    /***
     @Transactional 它能保证方法内多个数据库操作要么同时成功、要么同时失败。
     */
    @Override
    @Transactional
    public void save(Borrow obj) {
        //1.校验用户的积分是否足够
        String userNo = obj.getUserNo();
        //获取会员码
        User user = userMapper.getByUsername(userNo);
        if (Objects.isNull(user)) {
            throw new ServiceException("用户不存在");
        }
        //2.校园图书的数量是否足够
        //获取图书标准码
        Book book = bookMapper.getByNo(obj.getBookNo());
        if (Objects.isNull(book)) {
            throw new ServiceException("所借图书不存在");
        }
        //3.校验图书数量
        if (book.getNums() < 1) {
            throw new ServiceException("图书数量不足");
        }
        Integer account = user.getAccount();
        Integer score = book.getScore() * obj.getDays();// score = 借1本的积分 * 天数
        //4.校验用户账号余额
        if (score > account) {
            throw new ServiceException("用户积分不足");
        }
        //5.更新用户余额
        user.setAccount(user.getAccount() - score);
        userMapper.updateById(user);
        //6.更新图书的数量
        book.setNums(book.getNums() - 1);
        bookMapper.updateById(book);

        obj.setScore(score);
        //当前的日期加 days 得到归还的日期
        obj.setReturnDate(LocalDate.now().plus(obj.getDays(), ChronoUnit.DAYS));
        //7.新增借书记录
        borrowMapper.save(obj);
    }

    @Override
    public Borrow getById(Integer id) {
        return borrowMapper.getById(id);
    }

    @Override
    public void update(Borrow obj) {
        //修改了数据更新修改时间的操作
        obj.setUpdatetime(new Date());
        borrowMapper.updateById(obj);
    }

    @Override
    public void deleteById(Integer id) {
        borrowMapper.deleteById(id);
    }
}
