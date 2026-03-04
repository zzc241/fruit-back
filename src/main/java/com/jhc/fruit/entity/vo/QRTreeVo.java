package com.jhc.fruit.entity.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
public class QRTreeVo {
    @ApiModelProperty("用户id")
    private Long id;
    @ApiModelProperty("图片名")
    private String fileName;
}
