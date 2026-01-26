package com.jhc.fruit.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.math.BigDecimal;
import java.util.Date;

@Data
@TableName("fruit_price")
public class FruitPrice {
    private Long id;
    private String fruitName;
    private BigDecimal price;
    private String unit;
    private String origin;
    private String market;
    private Date priceDate;
    private Date updateTime;
    private Date createTime;
}
