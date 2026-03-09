package com.jhc.fruit.entity;

import java.time.LocalDateTime;

import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.fasterxml.jackson.annotation.JsonFormat;

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
    @ApiModelProperty(value = "创建时间(自动填充)")
    @TableField(fill = FieldFill.INSERT)
    @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss.SSS'Z'")
    private LocalDateTime createTime;
    @ApiModelProperty(value = "更新时间(自动填充)")
    @TableField(fill = FieldFill.INSERT_UPDATE)
    @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss.SSS'Z'")
    private LocalDateTime updateTime;
    @TableField(fill = FieldFill.INSERT)
    private Long createUser;
    @TableField(fill = FieldFill.INSERT_UPDATE)
    private Long updateUser;
}
