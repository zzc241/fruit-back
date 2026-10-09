package com.jhc.fruit.entity.vo;

import java.time.LocalDateTime;

import com.fasterxml.jackson.annotation.JsonProperty;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
public class DisasterVo {
    @ApiModelProperty
    private Long id;
    @ApiModelProperty(value = "灾害内容")
    @JsonProperty("content")
    private String content;
    @ApiModelProperty(value = "灾害类型")
    @JsonProperty("disaster_type")
    private String disasterType;
    @JsonProperty("area_id")
    @ApiModelProperty(value = "区域id")
    private Long areaId;
    @ApiModelProperty(value = "区域名称")
    private String areaName;
    @JsonProperty("orchard_id")
    @ApiModelProperty(value = "果园id")
    private Long orchardId;
    @ApiModelProperty(value = "果园名称")
    private String orchardName;
    @JsonProperty("user_id")
    @ApiModelProperty(value = "用户id")
    private Long userId;
    @ApiModelProperty(value = "用户名称")
    private String userName;
    @ApiModelProperty(value = "发现时间")
    private LocalDateTime findTime;
    @ApiModelProperty(value = "创建时间")
    private LocalDateTime createTime;
}

