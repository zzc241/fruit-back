package com.jhc.fruit.entity.vo;

import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.TableField;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import com.jhc.fruit.entity.BlockPosition;
import com.jhc.fruit.entity.FullIndicesMean;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.lang.reflect.Type;
import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;

@Data
public class AnalyzeVo {
    private Long orchardId;
    private Long areaId;
    //分析建议
    private String analyzeSuggestion;
    // 营养状态文本
    private String nutritionStatus;
    // 区块位置列表
    private List<BlockPosition> blockPositions;
    // 多光谱指数数据
    private FullIndicesMean spectralIndices;
    // 可上传MinIO的分析图（MultipartFile）
    private String analysisImage;
    private String mergeImage;
    @ApiModelProperty(value = "创建时间(自动填充)")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss.SSS", timezone = "GMT+8")
    private LocalDateTime createTime;

    // 初始化 Gson 实例（全局复用，避免重复创建）
    private static final Gson GSON = new GsonBuilder()
            .serializeNulls()
            .setDateFormat("yyyy-MM-dd HH:mm:ss.SSS")
            .create();

    /**
     * 将 JSON 字符串转换为 List<BlockPosition>
     * @param jsonStr JSON字符串（支持单个对象：{...} 或数组：[{...}, {...}]）
     * @return 转换后的 List<BlockPosition>，转换失败返回空列表
     */
    public static List<BlockPosition> convertJsonToBlockPositionList(String jsonStr) {
        // 1. 空值校验
        if (jsonStr == null || jsonStr.trim().isEmpty()) {
            return Collections.emptyList();
        }

        String trimJson = jsonStr.trim();
        try {
            // 2. 判断是 JSON 数组还是单个对象
            if (trimJson.startsWith("[")) {
                // 场景1：JSON 数组（[{...}, {...}]）
                Type listType = new TypeToken<List<BlockPosition>>() {}.getType();
                return GSON.fromJson(trimJson, listType);
            } else if (trimJson.startsWith("{")) {
                // 场景2：单个 JSON 对象（{...}），转为单元素列表
                BlockPosition blockPosition = GSON.fromJson(trimJson, BlockPosition.class);
                return Collections.singletonList(blockPosition);
            } else {
                return Collections.emptyList();
            }
        } catch (Exception e) {
            return Collections.emptyList();
        }
    }

    public void setBlockPositions(String blockPositions) {
        this.blockPositions = convertJsonToBlockPositionList(blockPositions);
    }

    public static FullIndicesMean convertJsonToSpectralIndices(String jsonStr) {
        // 1. 空值校验
        if (jsonStr == null || jsonStr.trim().isEmpty()) {
            return new FullIndicesMean();
        }

        String trimJson = jsonStr.trim();
        try {
            // 2. 仅处理单个 JSON 对象（FullIndicesMean 是单个对象，数组无意义）
            if (trimJson.startsWith("{")) {
                FullIndicesMean fullIndicesMean = GSON.fromJson(trimJson, FullIndicesMean.class);
                return fullIndicesMean;
            } else if (trimJson.startsWith("[")) {
                // JSON 数组：无法转换为单个 FullIndicesMean，返回空对象并打错误日志
                return new FullIndicesMean();
            } else {
                // 非法 JSON 格式（既不是 {} 也不是 []）
                return new FullIndicesMean();
            }
        } catch (Exception e) {
            // 捕获转换异常（比如字段类型不匹配、JSON 语法错误等）
            return new FullIndicesMean();
        }
    }

    public void setSpectralIndices(String spectralIndices) {
        this.spectralIndices = convertJsonToSpectralIndices(spectralIndices);
    }
}
