package com.jhc.fruit.entity.vo;

import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.time.LocalDateTime;
/**
 * @author : zyr
 * @date  : 2026/1/23
 *用于在前端显示用的，在Area的基础上将typeId和areaId转换成对应名字
 **/

@Data
public class AreaVo {

    private int id;
    private String name;
    private int size;
//    果树类型
    private String type;
    private LocalDateTime createTime;
    private LocalDateTime updateTime;
    private String description;
    private Long createUser;
    private Long updateUser;
    private Long empId;
}
