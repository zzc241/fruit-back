package com.jhc.fruit.entity;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

/**
 * 适配Python接口返回的响应实体
 */
@Data
@JsonIgnoreProperties(ignoreUnknown = true) // 忽略Python返回的未知字段
public class MultiSpectralResponse {
    private int code;
    private String msg;
    // 匹配Python返回的data结构（包含json_file_path和json_content）
    private PythonResponseData data;

    /**
     * Python接口返回的Data结构
     */
    @Data
    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class PythonResponseData {
        private String json_file_path; // Python返回的JSON文件路径
        private PythonFertilizerAnalysis json_content; // Python返回的分析结果
    }

    /**
     * Python返回的完整分析结果结构（补充所有字段）
     */
    @Data
    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class PythonFertilizerAnalysis {
        private String status;
        private String msg;
        private String fertilizer_level; // 水肥水平
        private String suggestion; // 建议文本

        // ========== 新增光谱指数字段（匹配Python返回的小写字段） ==========
        @JsonProperty("ndvi") // 映射Python的ndvi字段
        private Double ndvi;
        @JsonProperty("ndre")
        private Double ndre;
        @JsonProperty("osavi")
        private Double osavi;
        @JsonProperty("ndpi")
        private Double ndpi;
        @JsonProperty("kvi")
        private Double kvi;
        @JsonProperty("ksi")
        private Double ksi;
        @JsonProperty("tri")
        private Double tri;
        @JsonProperty("mtci")
        private Double mtci;
        @JsonProperty("cavi") // Python的caVI对应cavi
        private Double cavi;
        @JsonProperty("wi")
        private Double wi;
        @JsonProperty("ndwi")
        private Double ndwi;
        @JsonProperty("msi")
        private Double msi;
        @JsonProperty("ci_rededge")
        private Long ciRededge; // Python的ci_rededge
        @JsonProperty("cci")
        private Double cci;
        @JsonProperty("pri")
        private Double pri;
        @JsonProperty("sipi")
        private Double sipi;
        @JsonProperty("evi")
        private Double evi;
        @JsonProperty("lai")
        private Double lai;

        // ========== 新增地理/区块信息字段 ==========
        @JsonProperty("latitude")
        private Double latitude; // 纬度
        @JsonProperty("longitude")
        private Double longitude; // 经度
        @JsonProperty("altitude")
        private Double altitude; // 海拔
        @JsonProperty("full_block_code")
        private String fullBlockCode; // 完整区块编码
        @JsonProperty("big_block")
        private String bigBlock; // 大区块
        @JsonProperty("sub_block")
        private String subBlock; // 子区块
    }
}