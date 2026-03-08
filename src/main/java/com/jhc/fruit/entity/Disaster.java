package com.jhc.fruit.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@TableName("disaster")
public class Disaster {
    @ApiModelProperty
    @TableId(type = IdType.AUTO)
    private Long id;
    @ApiModelProperty(value = "灾害内容")
    private String content;
    @ApiModelProperty(value = "灾害类型")
    @JsonProperty("disaster_type")
    private String disasterType;
    @JsonProperty("area_id")
    @ApiModelProperty(value = "区域id")
    private Long areaId;
    @JsonProperty("orchard_id")
    @ApiModelProperty(value = "果园id")
    private Long orchardId;
    @JsonProperty("user_id")
    @ApiModelProperty(value = "用户id")
    private Long userId;
    @ApiModelProperty(value = "发现时间")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss.SSS", timezone = "GMT+8")
    private LocalDateTime findTime;
    @ApiModelProperty(value = "创建时间")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss.SSS", timezone = "GMT+8")
    private LocalDateTime createTime;
}
