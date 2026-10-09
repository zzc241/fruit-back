package com.jhc.fruit.entity.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.springframework.web.multipart.MultipartFile;

@Data
public class QRTreeVo {
//    @ApiModelProperty("ÓÃ»§id")
//    private Long id;
//    @ApiModelProperty("Í¼Æ¬Â·¾¶")
//    private String fileName;
    @ApiModelProperty("Í¼Æ¬")
    private MultipartFile imgFile;
}
