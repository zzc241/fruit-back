package com.jhc.fruit.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import lombok.Data;

/**
 * @author ：lcy
 * @description：TODO
 * @date ：2022/5/19 0019 20:25
 */
@Data
public class News {
    @TableId(type = IdType.ASSIGN_ID)
    private Long id;
    private String title;
    private String url;
    private String content;
    private int agreement;
    private String imgUrl;
}
