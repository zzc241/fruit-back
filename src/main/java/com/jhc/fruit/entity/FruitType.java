package com.jhc.fruit.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @author ：lcy
 * @description：TODO
 * @date ：2022/5/9 0009 9:38
 */
@Data
@TableName("fruit_type")
public class FruitType {
    @ApiModelProperty
    private Long id;
    @ApiModelProperty
    private String type;
}
