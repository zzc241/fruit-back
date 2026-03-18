package com.jhc.fruit.entity;

import lombok.Data;
import org.springframework.web.multipart.MultipartFile;
import java.util.List;

@Data
public class ApiResponse {
    private int code = 200;
    private String msg = "success";
    private ResponseData data;

    @Data
    public static class ResponseData {
        // 营养状态文本
        private String nutritionStatus;
        // 区块位置列表
        private List<BlockPosition> blockPositions;
        // 多光谱指数数据
        private FullIndicesMean spectralIndices;
        // 可上传MinIO的分析图（MultipartFile）
        private String analysisImage;
        private String mergeImage;
    }
}