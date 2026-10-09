package com.jhc.fruit.entity;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
@ApiModel(value = "任务图片")
public class TaskImg {
    @ApiModelProperty(value = "任务id")
    private Long id;
    @ApiModelProperty(value = "图片id")
    private String img;
    @ApiModelProperty(value = "任务id")
    private Long taskId;
}
