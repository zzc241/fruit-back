package com.jhc.fruit.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
@ApiModel(value = "果树组")
public class FruitGroup {
    @ApiModelProperty("果园组id")
    @TableId(type = IdType.AUTO)
    private Long id;
    @ApiModelProperty("果园id")
    private Long orchardId;
    @ApiModelProperty("组名")
    private String name;
    @ApiModelProperty("备注")
    private String description;
}
