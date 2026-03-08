package com.jhc.fruit.entity.vo;

import java.time.LocalDateTime;

import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.TableField;
import com.fasterxml.jackson.annotation.JsonFormat;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
public class CommentVo {
    @ApiModelProperty
    private Long id;
    @ApiModelProperty(value = "用户id")
    private Long userId;
    @ApiModelProperty(value = "果树id")
    private Long treeId;
    @ApiModelProperty(value = "果树种类")
    private String treeType;
    @ApiModelProperty(value = "果园id")
    private Long orchardId;
    @ApiModelProperty(value = "内容")
    private String content;
    @ApiModelProperty(value = "选择（好吃/不好）")
    private Integer choice;
    @ApiModelProperty(value = "创建时间")
    @TableField(fill = FieldFill.INSERT)
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss.SSS", timezone = "GMT+8")
    private LocalDateTime createTime;
    @ApiModelProperty(value = "好评率")
    private Integer goodRate;
}
