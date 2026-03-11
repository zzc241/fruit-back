package com.jhc.fruit.entity;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.List;

@Data
@ApiModel
public class AIWeeds {

    @Data
    @ApiModel
    public class WeedDetectionResponse {
        private Integer code;
        private String message;
        private DetectionResult result;
    }

    @Data
    @ApiModel
    public class DetectionResult {
        private List<WeedDetection> detections;
        @ApiModelProperty("图像尺寸：[height, width, channel]")
        private List<Integer> imageShape; // [height, width, channel]
    }

    @Data
    @ApiModel
    public class WeedDetection {
        private Long id;
        private Long aiId;
        @ApiModelProperty("种类代码")
        private Long classId;
        @ApiModelProperty("杂草种类")
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
    }

}
