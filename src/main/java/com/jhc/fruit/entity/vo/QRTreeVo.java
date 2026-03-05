package com.jhc.fruit.entity.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.springframework.web.multipart.MultipartFile;

@Data
public class QRTreeVo {
//    @ApiModelProperty("用户id")
//    private Long id;
//    @ApiModelProperty("图片路径")
//    private String fileName;
    @ApiModelProperty("图片")
    private MultipartFile imgFile;
}
