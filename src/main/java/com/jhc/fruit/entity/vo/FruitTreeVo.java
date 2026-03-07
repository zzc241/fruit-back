package com.jhc.fruit.entity.vo;

import java.sql.Date;
import java.time.LocalDateTime;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
* @author : zyr
* @date  : 2026/1/23
*用于在前端显示用的，在FruitTree的基础上将typeId和areaId转换成对应名字
 **/


@Data
@ApiModel
public class FruitTreeVo {
    @ApiModelProperty
    private Long id;
    @ApiModelProperty(value = "类型id")
    private Long typeId;
    @ApiModelProperty(value = "类型名称")
    private String type;
    @ApiModelProperty(value = "区域id")
    private Long areaId;
    @ApiModelProperty(value = "果实总数量")
    private Long countNum;
    @ApiModelProperty(value = "成熟个数")
    private Long ripeNum;
    @ApiModelProperty(value = "图片路径")
    private String img;
    @ApiModelProperty(value = "区域名")
    private String areaName;
    @ApiModelProperty(value = "健康状态")
    private Long healthCondition;
    @ApiModelProperty(value = "成熟度")
    private Double ripeDegree;


    @ApiModelProperty(value = "区域负责人姓名")
    private String areaManagerName;
    @ApiModelProperty(value = "操作方法")
    private String operationMethod;
    @ApiModelProperty(value = "操作时间")
    private LocalDateTime operationTime;

}
