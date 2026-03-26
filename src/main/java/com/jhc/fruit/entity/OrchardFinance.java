package com.jhc.fruit.entity;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@ApiModel(value = "OrchardFinance对象", description = "果园收支记录表")
public class OrchardFinance {

    @ApiModelProperty(value = "主键ID")
    private Long id;

    @ApiModelProperty(value = "金额")
    private Double amount;

    @ApiModelProperty(value = "收支类型 0-收入 1-支出")
    private Integer type;

    @ApiModelProperty(value = "用途 0-水费 1-肥料 2-农药 3-工资 4-电费 5-其他")
    private Integer usage;

    @ApiModelProperty(value = "用途说明（说明具体的使用情况）")
    private String usageDesc;

    @ApiModelProperty(value = "转账账户")
    private String transferUser;

    @ApiModelProperty(value = "收账账户")
    private String receiveUser;

    @ApiModelProperty(value = "汇款方式 0-微信 1-支付宝 2-银行 3-现金")
    private Integer payMethod;

    @ApiModelProperty(value = "果园ID")
    private Long orchardId;

    @ApiModelProperty(value = "创建时间")
    private LocalDateTime createTime;

    @ApiModelProperty(value = "更新时间")
    private LocalDateTime updateTime;
}