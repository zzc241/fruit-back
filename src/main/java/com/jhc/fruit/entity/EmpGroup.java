package com.jhc.fruit.entity;

import java.time.LocalDateTime;

import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.TableField;
import com.fasterxml.jackson.annotation.JsonFormat;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @author ：lcy
 * @description：TODO
 * @date ：2022/6/13 0013 9:25
 */
@Data
public class EmpGroup {
    @ApiModelProperty("组id")
    @TableField(fill = FieldFill.INSERT)
    private Long id;
    private Long groupId;
    private Long empId;
    private String groupName;
    private String content;
    private String description;
    private Long infoId;
    private Boolean isFinish;
    @ApiModelProperty(value = "创建时间(自动填充)")
    @TableField(fill = FieldFill.INSERT)
    @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss.SSS'Z'")
    private LocalDateTime createTime;

    @ApiModelProperty(value = "更新时间(自动填充)")
    @TableField(fill = FieldFill.INSERT_UPDATE)
    @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss.SSS'Z'")
    private LocalDateTime updateTime;
}
