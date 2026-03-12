package com.jhc.fruit.entity.vo;

import com.jhc.fruit.entity.Area;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.List;

@Data
@ApiModel
public class TreeTypeVo {
    private Area area;
    private List<TreeType> typeCount;

    @Data
    public static class TreeType{
        private String typeName;
        private Integer count;

        public TreeType(String type, Integer count) {
            this.typeName = type;
            this.count = count;
        }
    }
}
