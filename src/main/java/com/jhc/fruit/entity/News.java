package com.jhc.fruit.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @author ：lcy
 * @description：TODO
 * @date ：2022/5/19 0019 20:25
 */
@Data
public class News {
    @TableId(type = IdType.ASSIGN_ID)
    @ApiModelProperty
    private Long id;
    @ApiModelProperty("果园id")
    private Long orchardId;
    @ApiModelProperty("标题")
    private String title;
    @ApiModelProperty("原文链接")
    private String url;
    @ApiModelProperty("内容")
    private String content;
    @ApiModelProperty("点赞数")
    private int agreement;
    @ApiModelProperty("图片url")
    private String imgUrl;
}
