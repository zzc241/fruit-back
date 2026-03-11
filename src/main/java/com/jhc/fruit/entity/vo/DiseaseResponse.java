package com.jhc.fruit.entity.vo;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Data;

@Data
public class DiseaseResponse {
    
    private Integer code;
    private String msg;
    private String answer;        // 识别结果文本
    
    @JsonProperty("image_data")
    private String imageData;     // 图片十六进制串
}