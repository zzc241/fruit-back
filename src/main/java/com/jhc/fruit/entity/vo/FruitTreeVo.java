package com.jhc.fruit.entity.vo;

import com.baomidou.mybatisplus.annotation.TableField;
import lombok.Data;

/**
* @author : zyr
* @date  : 2026/1/23
*用于在前端显示用的，在FruitTree的基础上将typeId和areaId转换成对应名字
 **/


@Data
public class FruitTreeVo {
    private Long id;
    private String typen;
    private Long AreaId;
    private int countNum;
    private int ripeNum;
    private String img;
    private String areaName;
}
