package com.jhc.fruit.entity.vo;

import com.baomidou.mybatisplus.annotation.TableField;
import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;

@Data
@ApiModel
public class TaskVo {
    @ApiModelProperty(value = "任务id")
    private Long id;
    /*创建人姓名可能存在重名，区分需要用id*/
    @ApiModelProperty(value = "创建人id")
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
    @ApiModelProperty(value = "果园名称")
    private String orchardName;
    @ApiModelProperty(value = "区域名称")
    private String areaName;
    @ApiModelProperty(value = "创建人姓名")
    private String username;
    /*描述任务内容*/
    @ApiModelProperty(value = "任务内容")
    private String taskInfo;
    @ApiModelProperty(value = "任务标题")
    private String taskTitle;
    @ApiModelProperty(value = "任务类型")
    private String taskType;
    
    @ApiModelProperty(value = "任务范围")
    private Long groupId;
    @ApiModelProperty(value = "任务图片组id")
    private Long imgId;
    @ApiModelProperty(value = "复数图片URL")
    private List<String> imgsURL;
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss.SSS", timezone = "GMT+8")
    @ApiModelProperty(value = "完成时间")
    private LocalDateTime completionTime;
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss.SSS", timezone = "GMT+8")
    @ApiModelProperty(value = "任务结束时间")
    private LocalDateTime deadline;
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss.SSS", timezone = "GMT+8")
    @ApiModelProperty(value = "创建时间")
    private LocalDateTime createTime;
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss.SSS", timezone = "GMT+8")
    @ApiModelProperty(value = "更新时间")
    private LocalDateTime updateTime;
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss.SSS", timezone = "GMT+8")
    @ApiModelProperty(value = "发布时间")
    private LocalDateTime postTime;
    @ApiModelProperty(value = "任务状态")
    private Integer status;
//    @ApiModelProperty(value = "用户组id")
//    private Long empgroupId;
//    @ApiModelProperty(value = "用户组id列表")
//    private List<Long> empIds;
//    @ApiModelProperty(value = "员工姓名列表")
//    private List<String> empNames;

//    @ApiModelProperty(value = "员工组名称")
//    private String groupName;
//    @ApiModelProperty(value = "员工组描述")
//    private String groupDescription;
//    @ApiModelProperty(value = "员工组内容")
//    private String groupContent;

    @ApiModelProperty(value = "接收者id")
    private Long receiverId;
    @ApiModelProperty(value = "接收者名称")
    private String receiverName;
    @ApiModelProperty("优先级（0日常任务，1紧急任务）")
    private String priority;
    private Double salary;
    private ReportVo reportVo;
}
