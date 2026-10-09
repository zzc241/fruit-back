package com.jhc.fruit.entity;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class MultiSpectralData {
    @JsonProperty("??????")
    private String photoNo;

    @JsonProperty("fertilizer_analysis_json???????")
    private FertilizerAnalysis fertilizerAnalysis;

    @JsonProperty("????????????_base64")
    private String analysisImageBase64;
}