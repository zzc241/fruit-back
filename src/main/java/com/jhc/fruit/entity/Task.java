package com.jhc.fruit.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
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
    @TableId(type = IdType.ASSIGN_ID)
    @ApiModelProperty(value = "任务id")
    private Long id;
    @ApiModelProperty(value = "创建人id")
    private Long creatorId;
    @ApiModelProperty(value = "果园id")
    private Long orchardId;
    /*描述任务内容*/
    @ApiModelProperty(value = "任务内容")
    private String taskInfo;
    @ApiModelProperty(value = "任务标题")
    private String taskTitle;
    /*绑定员工组empGroup的groupId，用来表示发布范围*/
    @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss.SSS'Z'")
    @ApiModelProperty(value = "完成时间")
    private LocalDateTime completionTime;
    @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss.SSS'Z'")
    @ApiModelProperty(value = "创建时间")
    private LocalDateTime createTime;
    @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss.SSS'Z'")
    @ApiModelProperty(value = "更新时间")
    private LocalDateTime updateTime;
    @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss.SSS'Z'")
    @ApiModelProperty(value = "任务状态")
    private Integer status;
    @ApiModelProperty(value = "用户组id")
    private Long empgroupId;
}
