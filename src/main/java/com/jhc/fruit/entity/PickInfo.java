package com.jhc.fruit.entity;

import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.TableField;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.time.LocalDateTime;

/**
 * @author ：lcy
 * @description：通知信息
 * @date ：2022/5/28 0028 13:21
 */
@Data
public class PickInfo {
    private Long id;
    private String title;
    private String content;
    @TableField(fill = FieldFill.INSERT)
    private LocalDateTime createTime;
    @ApiModelProperty(value = "更新时间(自动填充)")
    @TableField(fill = FieldFill.INSERT_UPDATE)
    private LocalDateTime updateTime;
    @TableField(fill = FieldFill.INSERT)
    private Long createUser;
    @TableField(fill = FieldFill.INSERT_UPDATE)
    private Long updateUser;
    //接收通知信息的用户id
    private Long empId;
    private String empName;
    //判断用户是否已读（默认false：未读）
    private Boolean isRead;
    //判断任务是否完成（默认false：未完成）
    private Boolean isFinish;
}
