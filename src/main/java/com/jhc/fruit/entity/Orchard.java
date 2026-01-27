package com.jhc.fruit.entity;

import java.time.LocalDateTime;

import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonFormat;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
/**
 * @author ：zzc
 * @description：果园信息实体类
 * @date ：2026/1/23
 */
@Data
@TableName("orchard")
@ApiModel
public class Orchard {
    @ApiModelProperty("果园id")
    @TableId(type = IdType.AUTO)
    private Long id;
    @ApiModelProperty("果园名称")
    private String name;

    @ApiModelProperty("果园大小")
    private Long size;

    // @ApiModelProperty("关联的区域id")
    // private Long areaId;
    @ApiModelProperty("关联的果园管理员id")
    private Long empId;
    @ApiModelProperty("描述")
    private String description;
    @ApiModelProperty(value = "果园状态(未审核0，已审核1，禁用2）")
    private Integer status;
    @ApiModelProperty(value = "创建时间(自动填充)")
    @TableField(fill = FieldFill.INSERT)
    @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss.SSS'Z'")
    private LocalDateTime createTime;
    @ApiModelProperty(value = "更新时间(自动填充)")
    @TableField(fill = FieldFill.INSERT_UPDATE)
    @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss.SSS'Z'")
    private LocalDateTime updateTime;
    @ApiModelProperty("创建人")
    @TableField(fill = FieldFill.INSERT)
    private Long createUser;
    @ApiModelProperty("修改人")
    @TableField(fill = FieldFill.INSERT_UPDATE)
    private Long updateUser;
}
