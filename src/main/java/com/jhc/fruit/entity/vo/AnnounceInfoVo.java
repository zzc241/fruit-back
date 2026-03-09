package com.jhc.fruit.entity.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @Author : zzc
 * @Description : 获取通知信息的VO
 * 分成电脑端和小程序端
 * 电脑端需要所有参数
 * 小程序端只需要传title，isFinish，isRead三个参数，empId从登录信息获取
 * @Date : 2026/1/23
 */
@Data
public class AnnounceInfoVo {
    @ApiModelProperty(value = "标题")
    private String title;
    
    @ApiModelProperty(value = "员工ID")
    //接收通知信息的用户id
    private Long empId;
    @ApiModelProperty(value = "员工名称")
    private String empName;
    @ApiModelProperty("关联的果园id")
    private Long orchardId;
    @ApiModelProperty("关联的果园名称")
    private String orchardName;
    @ApiModelProperty("关联的组id")
    private Long groupId;
    @ApiModelProperty("关联的组名称")
    private String groupName;
    @ApiModelProperty("招聘需要的人数")
    private Long needNumber;
    @ApiModelProperty("已经招聘到的人数")
    private Long haveNumber;
    @ApiModelProperty("关联的创建消息者id")
    private Long senderId;
    @ApiModelProperty("关联的创建消息者名称")
    private String senderName;

    @ApiModelProperty(value = "是否完成")
    private Long isFinish;
    @ApiModelProperty(value = "是否已读")
    private Long isRead;

    @ApiModelProperty(value = "当前页码")
    private Integer page = 1;
    @ApiModelProperty(value = "每页数量")
    private Integer pageSize = 10;

    @ApiModelProperty(value = "id" , example = "1")
    private Long id;
    @ApiModelProperty(value = "内容")
    private String content;

    private Long createUser;
    private Long updateUser;

    //消息状态：0未审核，1已审核，2已撤销，3未通过
    private Integer status;
    //薪资
    private Long salary;
}