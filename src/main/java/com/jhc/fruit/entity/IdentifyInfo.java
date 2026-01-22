package com.jhc.fruit.entity;

import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
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
    @ApiModelProperty("创建时间")
    private LocalDateTime createTime;

    @TableField(fill = FieldFill.INSERT)
    @ApiModelProperty("创建者")
    private Long createUser;

    @TableField(fill = FieldFill.INSERT_UPDATE)
    @ApiModelProperty("修改时间")
    private LocalDateTime updateTime;

    @TableField(fill = FieldFill.INSERT_UPDATE)
    @ApiModelProperty("修改者")
    private Long updateUser;


}
