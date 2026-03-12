package com.jhc.fruit.entity.vo;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import java.util.Map;

@Data
public class MaturityResponse {
    
    private Integer code;
    private String msg;
    private String answer;
    
    @JsonProperty("image_data")
    private String imageData;  // 新增：图片十六进制数据
    
    @JsonProperty("ripe_stats")
    private RipeStats ripeStats;
    
    @Data
    public static class RipeStats {
        @JsonProperty("total_fruits")
        private Integer totalFruits;
        
        @JsonProperty("maturity_distribution")
        private Map<String, Integer> maturityDistribution;  // 新增：成熟度分布
        
        @JsonProperty("ripe_fruits")
        private Integer ripeFruits;  // 新增：成熟果实数量
        
        @JsonProperty("ripe_rate")
        private Double ripeRate;  // 熟果率，JSON里是 50 表示 50%
    }
}