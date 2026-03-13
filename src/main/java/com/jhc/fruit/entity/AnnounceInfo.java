package com.jhc.fruit.entity;

import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.fasterxml.jackson.annotation.JsonFormat;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.time.LocalDateTime;

/**
 * @author ：lcy
 * @description：通知信息
 * @date ：2022/5/28 0028 13:21
 */
@Data
public class AnnounceInfo {
    // ALTER TABLE pick_info AUTO_INCREMENT = 21;设置自增起始值
    @TableId(type = IdType.AUTO)
    private Long id;
    private String title;
    private String content;
    
    //接收通知信息的用户id
    @ApiModelProperty(value = "员工ID")
    @TableField("emp_id")
    private Long empId;
    @TableField("need_number")
    private Long needNumber;
    @ApiModelProperty("已经招聘到的人数")
    @TableField("have_number")
    private Long haveNumber;
    @ApiModelProperty("关联果园id")
    @TableField("orchard_id")
    private Long orchardId;
    @ApiModelProperty("关联的区域id")
    @TableField("area_id")
    private Long areaId;
    //发送通知信息的用户id
    @ApiModelProperty("发送通知信息的用户id")
    @TableField("sender_id")
    private Long senderId;
    @ApiModelProperty("关联的组id")
    @TableField("group_id")
    private Long groupId;
    //判断用户是否已读（默认false：未读）
    @ApiModelProperty(value = "是否已读")
    @TableField("is_read")
    private Long isRead;
    //判断任务是否完成（默认false：未完成）
    @ApiModelProperty(value = "是否完成")
    @TableField("is_finish")
    private Long isFinish;

    //消息状态：0未审核，1已审核，2已撤销，3未通过
    private Integer status;
    //薪资
    private Long salary;


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
}
