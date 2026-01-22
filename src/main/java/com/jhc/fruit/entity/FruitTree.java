package com.jhc.fruit.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

/**
 * @author ：lcy
 * @description：TODO
 * @date ：2022/5/17 0017 9:30
 */
@Data
@TableName("fruit_tree")
public class FruitTree {
    private Long id;
    private Long typeId;
    @TableField("count_num")
    private int countNum;
    @TableField("ripe_num")
    private int ripeNum;

    private String img;
    @TableField("area_id")
    private int areaId;

    private String areaName;

}
