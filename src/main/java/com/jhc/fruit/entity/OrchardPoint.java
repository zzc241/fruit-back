package com.jhc.fruit.entity;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
@ApiModel(value = "果园坐标（50个坐标点）")
public class OrchardPoint {
    private Long id;
    @ApiModelProperty(value = "果园id")
    private Long orchardId;
    @ApiModelProperty(value = "坐标点json")
    private String points;
}
