package com.jhc.fruit.entity;

import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@ApiModel
@TableName("fert_water")
public class FertWater {

    @ApiModelProperty(value = "id")
    private Long id;
    @ApiModelProperty(value = "类型")
    private String type;
    @ApiModelProperty(value = "施用量")
    private Integer num;
    @ApiModelProperty(value = "unit")
    private String unit;
    @ApiModelProperty(value = "果树id")
    private Long treeId;
    @ApiModelProperty(value = "操作时间")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss.SSS", timezone = "GMT+8")
    private LocalDateTime operationTime;
    @ApiModelProperty(value = "操作方式")
    private String operationMethod;
    @ApiModelProperty(value = "创建时间")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss.SSS", timezone = "GMT+8")
    @TableField(fill = FieldFill.INSERT)
    private LocalDateTime createTime;
    @ApiModelProperty(value = "更新时间")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss.SSS", timezone = "GMT+8")
    @TableField(fill = FieldFill.INSERT_UPDATE)
    private LocalDateTime updateTime;
    @ApiModelProperty(value = "创建用户")
    private Long createUser;
    @ApiModelProperty(value = "更新用户")
    private Long updateUser;
}
