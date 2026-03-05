package com.jhc.fruit.entity;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
@ApiModel
public class Comment {
    @ApiModelProperty
    private Long id;
    @ApiModelProperty(value = "用户id")
    private Long userId;
    @ApiModelProperty(value = "果树id")
    private Long treeId;
    @ApiModelProperty(value = "果园id")
    private Long orchardId;
    @ApiModelProperty(value = "内容")
    private String comment;
    @ApiModelProperty(value = "选择（好吃/不好）")
    private Integer choice;
}
