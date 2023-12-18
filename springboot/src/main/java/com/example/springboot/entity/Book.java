package com.example.springboot.entity;

import lombok.Data;

import java.io.Serializable;
import java.util.List;

/****
 * @description book 实体类
*/

@Data
public class Book extends BookEntity implements Serializable {
    private static final long serialVersionUID = 1L;
    /**
     * 名称
     */
    private String name;

    /**
     * 描述
     */
    private String description;

    /**
     * 出版日期
     */
    private String publishDate;

    /**
     * 作者
     */
    private String author;

    /**
     * 出版社
     */
    private String publisher;

    /**
     * 分类
     */
    private String category;

    /**
     * 标准码
     */
    private String bookNo;

    /**
     * 封面
     */
    private String cover;

    //前端向后端传递一个数组
    private List<String> categories;

    //积分
    private Integer score;

    //数量
    private Integer nums;
}