package com.jhc.fruit.entity.vo;

import java.time.LocalDateTime;

import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.fasterxml.jackson.annotation.JsonFormat;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @author ：zzc
 * @description：果园管理与前端交互的实体类(加了分页参数和有关id的name字段)
 * @date ：2026/1/26
 */
@Data
public class OrchardVo {
    @ApiModelProperty("果园id")
    @TableId(type = IdType.AUTO)
    private Long id;
    @ApiModelProperty("果园名称")
    private String name;

    @ApiModelProperty("果园大小")
    private Long size;

    @ApiModelProperty("关联的区域名称")
    private String areaName;
    @ApiModelProperty("关联的果园管理员id")
    private Long empId;
    @ApiModelProperty("关联的果园管理员名称")
    private String empName;
    @ApiModelProperty("描述")
    private String description;
    @ApiModelProperty(value = "果园状态(未审核0，已审核1，禁用2）")
    private Integer status;
    @ApiModelProperty(value = "创建时间(自动填充)")
    @TableField(fill = FieldFill.INSERT)
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss.SSS", timezone = "GMT+8")
    private LocalDateTime createTime;
    @ApiModelProperty(value = "更新时间(自动填充)")
    @TableField(fill = FieldFill.INSERT_UPDATE)
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss.SSS", timezone = "GMT+8")
    private LocalDateTime updateTime;
    @ApiModelProperty("创建人")
    @TableField(fill = FieldFill.INSERT)
    private Long createUser;
    @ApiModelProperty("修改人")
    @TableField(fill = FieldFill.INSERT_UPDATE)
    private Long updateUser;
    @ApiModelProperty("地址")
    private String address;
    @ApiModelProperty(value = "当前页码")
    private Integer page = 1;
    @ApiModelProperty(value = "每页数量")
    private Integer pageSize = 10;
}
