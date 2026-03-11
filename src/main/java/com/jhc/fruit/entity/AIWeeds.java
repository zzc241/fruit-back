package com.jhc.fruit.entity;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.List;

@Data
@ApiModel
public class AIWeeds {

    @Data
    @ApiModel
    public static class WeedDetectionResponse {
        private Integer code;
        private String message;
        private DetectionResult result;
    }

    @Data
    @ApiModel
    public static class DetectionResult {
        private List<WeedDetection> detections;
        @ApiModelProperty("图像尺寸：[height, width, channel]")
        @JsonProperty("image_shape")
        private List<Integer> imageShape;
    }

    @Data
    @ApiModel
    public static class WeedDetection {
        private Long id;
        private Long treeId;
        @ApiModelProperty("种类代码")
        @JsonProperty("class_id") // 映射JSON的class_id
        private Long classId;
        @ApiModelProperty("杂草种类")
        @JsonProperty("class_name") // 映射JSON的class_name
        private String className;
        @ApiModelProperty("置信度")
        private Double confidence;
        @ApiModelProperty("坐标x_左上角")
        private Double x1;
        @ApiModelProperty("坐标y_左上角")
        private Double y1;
        @ApiModelProperty("坐标x_右下角")
        private Double x2;
        @ApiModelProperty("坐标y_右下角")
        private Double y2;
        @ApiModelProperty("原始图片")
        private String originImg;
        @ApiModelProperty("处理图片")
        private String outputImg;
    }

}
