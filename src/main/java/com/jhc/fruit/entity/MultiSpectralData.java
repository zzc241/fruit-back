package com.jhc.fruit.entity;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class MultiSpectralData {
    @JsonProperty("照片编号")
    private String photoNo;

    @JsonProperty("fertilizer_analysis_json全部数据")
    private FertilizerAnalysis fertilizerAnalysis;

    @JsonProperty("全维度多光谱分析图_base64")
    private String analysisImageBase64;
}