package com.jhc.fruit.entity.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.List;

@Data
@ApiModel
public class DisasterTypeVo {
    @ApiModelProperty(value = "灾害大类（1气象，2病害，3虫害)")
    private Long typeId;
    private List<DisasterType> typeCount;

    @Data
    public static class DisasterType{
        private String typeName;
        private Integer count;

        public DisasterType(String type, Integer count) {
            this.typeName = type;
            this.count = count;
        }
    }
}
