package com.jhc.fruit.entity.vo;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class MaturityResponse {
    
    private Integer code;
    private String msg;
    private String answer;
    
    @JsonProperty("ripe_stats")  // 对应JSON中的字段名
    private RipeStats ripeStats;
    
    @Data
    public static class RipeStats {
        @JsonProperty("total_fruits")
        private Integer totalFruits;
        
        @JsonProperty("ripe_rate")
        private Double ripeRate;  // 熟果率，可能是小数，如 0.85 表示 85%
    }
}