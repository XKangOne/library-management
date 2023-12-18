package com.example.springboot.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.time.LocalDate;
import java.util.Date;

/***
 * @description: 借书实体类
 * @author: yk
 **/
@Data
public class Retur {
    /**
     * id
     */
    private Integer id;

    /**
     * 图书名称
     */
    private String bookName;

    /**
     * 图书标准码
     */
    private String bookNo;

    /**
     * 会员码
     */
    private String userNo;

    /**
     * 用户名称
     */
    private String userName;
    /**
     * 用户联系方式
     */
    private String userPhone;
    /**
     * 创建时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd",timezone = "GMT+8")
    private Date createtime;
    /**
     * 更新时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd",timezone = "GMT+8")
    private Date updatetime;
    /**
     * 借书积分
     */
    private Integer score;
    //借书状态
    private String status;
    //借书天数
    private Integer days;
    //归还日期
    private LocalDate returnDate;
    //实际归还日期
    private LocalDate realDate;
}
