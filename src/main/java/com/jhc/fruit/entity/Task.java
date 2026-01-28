package com.jhc.fruit.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import io.swagger.annotations.ApiOperation;
import lombok.Data;

import java.time.LocalDateTime;


/**
 * @author ：zyr
 * &#064;description：任务表
 * &#064;date  ：2026/1/26
 */

@Data
@ApiModel(value = "任务表")
public class Task {
    @ApiModelProperty(value = "任务id")
    private Long id;
    @ApiModelProperty(value = "创建人id")
    private Long creatorId;
    /*描述任务内容*/
    @ApiModelProperty(value = "任务内容")
    private String taskInfo;
    @ApiModelProperty(value = "任务标题")
    private String taskTitle;
    /* 0一次性发布/1定时发布 */
    @ApiModelProperty(value = "任务类型")
    private Integer taskType;
    /*在存储服务器的相对路径*/
    @ApiModelProperty(value = "任务图片")
    private String img;
    /*绑定员工组empGroup的groupId，用来表示发布范围*/
    @ApiModelProperty(value = "发布范围")
    private Long taskScope;
    @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss.SSS'Z'")
    @ApiModelProperty(value = "完成时间")
    private LocalDateTime completionTime;
    @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss.SSS'Z'")
    @ApiModelProperty(value = "截止时间")
    private LocalDateTime deadline;
    @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss.SSS'Z'")
    @ApiModelProperty(value = "发布时间")
    private LocalDateTime postTime;
    @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss.SSS'Z'")
    @ApiModelProperty(value = "创建时间")
    private LocalDateTime createTime;
    @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss.SSS'Z'")
    @ApiModelProperty(value = "更新时间")
    private LocalDateTime updateTime;
    /*任务状态（0未发布/1未完成/2完成/3截止）*/
    @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss.SSS'Z'")
    @ApiModelProperty(value = "任务状态")
    private Integer status;
    /*定时发布的时间*/
    @ApiModelProperty(value = "定时发布")
    private String schedule;
}
