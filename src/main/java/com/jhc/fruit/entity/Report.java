package com.jhc.fruit.entity;

import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.TableField;
import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.Date;

@Data
@ApiModel
public class Report {
    @ApiModelProperty
    private Long id;
    @ApiModelProperty(value = "用户id")
    private Long userId;
    @ApiModelProperty(value = "果树id")
    private Long treeId;
    @ApiModelProperty(value = "报告内容")
    private String content;
    @ApiModelProperty(value = "创建日期")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss.SSS", timezone = "GMT+8")
    @TableField(fill = FieldFill.INSERT)
    private Date createTime;
}
