package com.jhc.fruit.entity;

import lombok.Data;
import java.util.List;

@Data
public class FertilizerAnalysis {
    private String photo_prefix;
    private String analysis_time;
    private String full_status_text;
    private FullIndicesMean full_indices_mean;
    private FertilizerQuantitativePred fertilizer_quantitative_pred;
    private List<BlockPosition> block_position;
}