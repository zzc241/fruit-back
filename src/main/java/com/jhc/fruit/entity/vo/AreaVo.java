package com.jhc.fruit.entity.vo;

import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.jhc.fruit.entity.Employee;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;

/**
 * @author : zyr
 * @date  : 2026/1/23
 *用于在前端显示用的，在Area的基础上将typeId和areaId转换成对应名字
 **/

@Data
public class AreaVo {
    @ApiModelProperty("地区id")
    @TableId(type = IdType.AUTO)
    private Long id;
    @ApiModelProperty
    private Long orchardId;
    @ApiModelProperty("地区名称")
    private String name;
    @ApiModelProperty("区域大小")
    private int size;
    @ApiModelProperty("果树类型")
    private String FruitType;
    @ApiModelProperty(value = "创建时间(自动填充)")
    @TableField(fill = FieldFill.INSERT)
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss.SSS", timezone = "GMT+8")
    private LocalDateTime createTime;
    @ApiModelProperty(value = "更新时间(自动填充)")
    @TableField(fill = FieldFill.INSERT_UPDATE)
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss.SSS", timezone = "GMT+8")
    private LocalDateTime updateTime;
    @ApiModelProperty("描述")
    private String description;
    @ApiModelProperty("创建人id")
    @TableField(fill = FieldFill.INSERT)
    private Long createUser;
    @ApiModelProperty("创建人名称")
    private String createUserName;
    @ApiModelProperty("修改人id")
    @TableField(fill = FieldFill.INSERT_UPDATE)
    private Long updateUser;
    @ApiModelProperty("负责人id")
    private Long empId;
    @ApiModelProperty("负责人")
    private String empName;

    @ApiModelProperty("果园名称")
    private String orchardName;
    @ApiModelProperty("地区类型")
    private String type;
    @ApiModelProperty("上一次更新用户")
    private String updateUserName;
    

    @ApiModelProperty("果树数量")
    private Long fruitCount;
    @ApiModelProperty(value = "员工组id")
    private List<Employee> employeeList;
}
