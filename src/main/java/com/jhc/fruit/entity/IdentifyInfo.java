package com.jhc.fruit.entity;

import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonFormat;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.time.LocalDateTime;

/**
 * @author ：lcy
 * @description：TODO
 * @date ：2022/5/10 0010 13:48
 */
@Data
@TableName("identify_info")
@ApiModel
public class IdentifyInfo {
    @ApiModelProperty("识别信息id")
    private Long id;
    @ApiModelProperty("果园id")
    private Long orchardId;
    @ApiModelProperty("果蔬数量")
    private int fruitNum;
    @ApiModelProperty("水果名称")
    private String fruitName;
    @ApiModelProperty("处理后水果定位的图片")
    private String imgCount;
    private String imgRipe;
    @ApiModelProperty("成熟水果数量")
    private int ripeNum;
    private Long treeId;
    private int treeRipePercent;

    @TableField(fill = FieldFill.INSERT)
    @ApiModelProperty("创建者")
    private Long createUser;

    @ApiModelProperty(value = "创建时间(自动填充)")
    @TableField(fill = FieldFill.INSERT)
    @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss.SSS'Z'")
    private LocalDateTime createTime;

    @ApiModelProperty(value = "更新时间(自动填充)")
    @TableField(fill = FieldFill.INSERT_UPDATE)
    @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss.SSS'Z'")
    private LocalDateTime updateTime;

    @TableField(fill = FieldFill.INSERT_UPDATE)
    @ApiModelProperty("修改者")
    private Long updateUser;


}
