package com.jhc.fruit.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
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
    private int countNum;
    @TableField("ripe_num")
    private int ripeNum;
    @ApiModelProperty("图片路径")
    private String img;
    @TableField("area_id")
    private int areaId;
}
