package com.jhc.fruit.entity.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @author : zzc
 * @description : TODO
 * @date : 2026/1/23
 */
@Data
public class PickInfoVo {
    @ApiModelProperty(value = "id" , example = "1")
    private Long id;
    @ApiModelProperty(value = "标题")
    private String title;
    @ApiModelProperty(value = "内容")
    private String content;

    private Long createUser;
    private Long updateUser;
    //接收通知信息的用户id
    @ApiModelProperty(value = "员工id")
    private Long empId;
    @ApiModelProperty(value = "员工名称")
    private String empName;
    //判断用户是否已读（默认false：未读）
    @ApiModelProperty(value = "是否已读")
    private Boolean isRead;
    //判断任务是否完成（默认false：未完成）
    @ApiModelProperty(value = "是否完成")
    private Boolean isFinish;
}