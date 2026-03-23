package com.jhc.fruit.entity.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.List;
import java.util.Map;

@Data
public class AIWeedVo {
    @ApiModelProperty("处理图片")
    private String outputImg;
    @ApiModelProperty("杂草种类及其数量统计")
    private List<Map<String, Integer>> classCount;
    @ApiModelProperty("平均置信度")
    private Double confidence;
}
