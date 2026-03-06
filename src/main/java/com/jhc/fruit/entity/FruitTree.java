package com.jhc.fruit.entity;

import java.sql.Date;

import com.baomidou.mybatisplus.annotation.*;
import com.fasterxml.jackson.annotation.JsonFormat;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @author ：lcy
 * @description：TODO
 * @date ：2022/5/17 0017 9:30
 */
@Data
@TableName("fruit_tree")
public class FruitTree {
    @ApiModelProperty("果树id")
    @TableId(type = IdType.AUTO)
    private Long id;
    @ApiModelProperty("果园id")
    private String orchardId;
    @ApiModelProperty("果树类型")
    @TableField("type")
    private Long type;
    @TableField("count_num")
    private Long countNum;
    @TableField("ripe_num")
    private Long ripeNum;
    @ApiModelProperty("图片路径")
    private String img;
    @TableField("area_id")
    private Long areaId;
    @TableField("health_condition")
    private Long healthCondition;
    @ApiModelProperty("更新时间（自动更新）")
    @TableField(fill = FieldFill.INSERT_UPDATE)
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss.SSS", timezone = "GMT+8")
    private Date updateTime;
    @ApiModelProperty("创建时间（自动填充）")
    @TableField(fill = FieldFill.INSERT)
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss.SSS", timezone = "GMT+8")
    private Date createTime;
}
