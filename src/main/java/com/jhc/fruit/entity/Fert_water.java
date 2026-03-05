package com.jhc.fruit.entity;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
@ApiModel
public class Fert_water {

    @ApiModelProperty(value = "id")
    private Long id;
    @ApiModelProperty(value = "类型")
    private String type;
    @ApiModelProperty(value = "施用量")
    private Integer num;
    @ApiModelProperty(value = "unit")
    private String unit;
    @ApiModelProperty(value = "果树id")
    private Long treeId;
}
