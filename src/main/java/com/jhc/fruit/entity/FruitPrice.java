package com.jhc.fruit.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.math.BigDecimal;
import java.util.Date;

@Data
@TableName("fruit_price")
@ApiModel
public class FruitPrice {
    @TableId(type = IdType.AUTO)
    private Long id;
    @ApiModelProperty("水果名称/品种id")
    private Long type_id;
    @ApiModelProperty(value = "水果名称/品种", example = "柑桔")
    private String fruitName;
    @ApiModelProperty("水果价格（元，保留2位小数）")
    private BigDecimal price;
    @ApiModelProperty("计价单位（如：元/斤、元/公斤）")
    private String unit;
    @ApiModelProperty("产地（如：山东烟台、云南昆明）")
    private String origin;
    @ApiModelProperty("交易市场名称（如：北京新发地、上海江桥）")
    private String market;
    @ApiModelProperty("价格日期（记录该价格的日期）")
    @JsonFormat(pattern = "yyyy-MM-dd", timezone = "GMT+8")
    private Date priceDate;
    @ApiModelProperty("更新时间（自动更新）")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss.SSS", timezone = "GMT+8")
    private Date updateTime;
    @ApiModelProperty("创建时间（自动填充）")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss.SSS", timezone = "GMT+8")
    private Date createTime;
}
