package com.jhc.fruit.entity;

import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
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
    @TableField("creator_id")
    private Long creatorId;
    @ApiModelProperty(value = "果园id")
    @TableField("orchard_id")
    private Long orchardId;
    @ApiModelProperty(value = "区域id")
    @TableField("area_id")
    private Long areaId;
    @ApiModelProperty(value = "果树id")
    @TableField("tree_id")
    private Long treeId;
    /*描述任务内容*/
    @ApiModelProperty(value = "任务内容")
    private String taskInfo;
    @ApiModelProperty(value = "任务标题")
    private String taskTitle;
    @ApiModelProperty(value = "任务类型")
    private String taskType;

    /*绑定员工组empGroup的groupId，用来表示发布范围*/
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss.SSS", timezone = "GMT+8")
    @ApiModelProperty(value = "完成时间")
    private LocalDateTime completionTime;
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss.SSS", timezone = "GMT+8")
    @ApiModelProperty(value = "任务结束时间")
    private LocalDateTime deadline;
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss.SSS", timezone = "GMT+8")
    @ApiModelProperty(value = "发布时间")
    private LocalDateTime postTime;
    @ApiModelProperty(value = "任务状态")
    private Integer status;
    @ApiModelProperty(value = "创建时间(自动填充)")
    @TableField(fill = FieldFill.INSERT)
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss.SSS", timezone = "GMT+8")
    private LocalDateTime createTime;
    @ApiModelProperty(value = "更新时间(自动填充)")
    @TableField(fill = FieldFill.INSERT_UPDATE)
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss.SSS", timezone = "GMT+8")
    private LocalDateTime updateTime;
    @TableField(fill = FieldFill.INSERT)
    private Long createUser;
    @TableField(fill = FieldFill.INSERT_UPDATE)
    private Long updateUser;
    @ApiModelProperty("优先级（0日常任务，1紧急任务）")
    private Integer priority;
    @ApiModelProperty("失败原因")
    private String failure;
    private Double salary;
    @ApiModelProperty(value = "接收者id")
    private Long receiverId;
    private Long groupId;
    private Long reportId;
}
