package com.jhc.fruit.entity;

import com.baomidou.mybatisplus.annotation.*;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.beans.Transient;
import java.time.LocalDateTime;

/**
 * @author ：lcy
 * @description：TODO
 * @date ：2022/5/9 0009 8:16
 */
@Data
@TableName("area_info")
@ApiModel
public class Area {
    @ApiModelProperty("地区id")
    @TableId(type = IdType.AUTO)
    private int id;
    @ApiModelProperty("地区名称")
    private String name;

    private int size;

    private String type;
    private Long typeId;
    @ApiModelProperty("创建时间")
    @TableField(fill = FieldFill.INSERT)
    private LocalDateTime createTime;
    @ApiModelProperty("修改时间")
    @TableField(fill = FieldFill.INSERT_UPDATE)
    private LocalDateTime updateTime;
    @ApiModelProperty("描述")
    private String description;

    @ApiModelProperty("创建人")
    @TableField(fill = FieldFill.INSERT)
    private Long createUser;
    @ApiModelProperty("修改人")
    @TableField(fill = FieldFill.INSERT_UPDATE)
    private Long updateUser;
    private Long empId;
}
