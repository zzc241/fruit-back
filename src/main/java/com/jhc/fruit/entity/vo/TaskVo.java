package com.jhc.fruit.entity.vo;

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
    private Long orchardId;
    @ApiModelProperty(value = "创建人姓名")
    private String username;
    /*描述任务内容*/
    @ApiModelProperty(value = "任务内容")
    private String taskInfo;
    @ApiModelProperty(value = "任务标题")
    private String taskTitle;
    @ApiModelProperty(value = "任务图片组id")
    private Long imgId;
    @ApiModelProperty(value = "复数图片URL")
    private List<String> imgsURL;
    @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss.SSS'Z'")
    @ApiModelProperty(value = "完成时间")
    private LocalDateTime completionTime;
    @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss.SSS'Z'")
    private LocalDateTime postTime;
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
    @ApiModelProperty(value = "用户组id列表")
    private List<Long> empIds;
}
