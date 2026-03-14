package com.jhc.fruit.entity.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import io.swagger.annotations.ApiOperation;
import lombok.Data;

import java.util.List;
import java.util.Map;

@Data
@ApiModel
public class OwnTaskVo {
    private Long empId;
    private Integer tasksCount;
    @ApiModelProperty("统计各个类型任务的个数")
    private List<Map<String, Integer>> typeCount;
    @ApiModelProperty("施肥任务统计(各个肥料的用量)")
    private List<Map<String, Double>> fertCount;
    @ApiModelProperty("浇水任务统计(总浇水用量)")
    private Double waterCount;
    @ApiModelProperty("农药任务统计(各个类型的农药的用量)")
    private List<Map<String, Double>> pesticideCount;
}
