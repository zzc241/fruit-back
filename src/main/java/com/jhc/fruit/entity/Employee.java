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

@Data
@ApiModel
public class Employee{
    @ApiModelProperty(value = "员工id")
    private Long id;

    @ApiModelProperty(value = "果园id")
    private Long orchardId;

    @ApiModelProperty(value = "账号" , example = "zzc")
    private String username;

    @ApiModelProperty(value = "姓名")
    private String name;
    @ApiModelProperty(value = "密码", example = "123456")
    private String password;
    @ApiModelProperty(value = "手机号")
    private String phone;
    @ApiModelProperty(value = "性别(1男0女)")
    private Long sex;
    @ApiModelProperty(value = "身份证号")
    private String idCard;
    @ApiModelProperty(value = "员工状态(可用：1，禁用：0)")
    private Integer status;
    @ApiModelProperty(value = "创建时间(自动填充)")
    @TableField(fill = FieldFill.INSERT)
    @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss.SSS'Z'")
    private LocalDateTime createTime;

    @ApiModelProperty(value = "更新时间(自动填充)")
    @TableField(fill = FieldFill.INSERT_UPDATE)
    @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss.SSS'Z'")
    private LocalDateTime updateTime;

    @ApiModelProperty(value = "创建者")
    @TableField(fill = FieldFill.INSERT)
    private Long createUser;
    @ApiModelProperty(value = "更新者")
    @TableField(fill = FieldFill.INSERT_UPDATE)
    private Long updateUser;

    private String headImg;
    @ApiModelProperty(value = "1:超级管理员 ， 2 ： 果园管理员 ， 3 ： 农户 ， 4 ： 消费者")
    @TableField("is_admin")  // 改为下划线
    private Integer isAdmin; 

    private String openId;

}
