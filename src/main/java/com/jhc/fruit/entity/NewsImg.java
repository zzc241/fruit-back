package com.jhc.fruit.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import lombok.Data;

/**
 * @author ：lcy
 * @description：TODO
 * @date ：2022/5/26 0026 14:58
 */
@Data
public class NewsImg {

    private Long id;
    @TableField("img_url")
    private String imgUrl;
    @TableField("news_id")
    private Long newsId;
}
