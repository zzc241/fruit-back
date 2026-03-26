package com.jhc.fruit.entity.vo;

import lombok.Data;

@Data
public class HealthTreeVo {
    //健康株树
    private Integer healthTree;
    //病树数量
    private Integer disasterTree;
    //健康株树百分比
    private Double health;
    //病树百分比
    private Double disaster;
}
