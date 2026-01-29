package com.jhc.fruit.entity;

import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.TableField;
import com.fasterxml.jackson.annotation.JsonFormat;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.time.LocalDateTime;

/**
 * @author ：lcy
 * @description：TODO
 * @date ：2022/5/3 0003 8:37
 */
@Data
@ApiModel
public class Camera {
    @ApiModelProperty(value="摄像头id，唯一id")
    private int id;

    @ApiModelProperty(value = "果园id")
    private Long orchardId;

    @ApiModelProperty(value="区id，标识摄像头属于哪一块")
    private int areaId;

    @ApiModelProperty(value = "创建时间(自动填充)")
    @TableField(fill = FieldFill.INSERT)
    @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss.SSS'Z'")
    private LocalDateTime createTime;

    @ApiModelProperty(value = "更新时间(自动填充)")
    @TableField(fill = FieldFill.INSERT_UPDATE)
    @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss.SSS'Z'")
    private LocalDateTime updateTime;

    @ApiModelProperty(value = "创建人员的id")
    @TableField(fill = FieldFill.INSERT)
    private Long createUser;

    @ApiModelProperty(value = "修改人员的id")
    @TableField(fill = FieldFill.INSERT_UPDATE)
    private Long updateUser;

    private String url;
}
