package com.jhc.fruit.entity;

import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.time.LocalDateTime;

@TableName("analyze")
@Data
public class Analyze {
    private Long id;
    private String nutritionStatus;
    private Long orchardId;
    private Long areaId;
    private String blockPositions;
    private String spectralIndices;
    private String analysisImage;
    private String mergeImage;
    private String analyzeSuggestion;
    @ApiModelProperty(value = "创建时间(自动填充)")
    @TableField(fill = FieldFill.INSERT)
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss.SSS", timezone = "GMT+8")
    private LocalDateTime createTime;
    private String analyze;
    private String analyzeAI;

    public void updateSpectralIndices(FullIndicesMean fullIndicesMean) {
        this.spectralIndices = toJsonByGson(fullIndicesMean);
    }

    /**
     * 将 FullIndicesMean 对象转为 JSON 字符串（Gson 实现）
     * @return 格式化后的 JSON 字符串
     */
    private static String toJsonByGson(Object object) {
        // 1. 创建 Gson 实例，开启格式化输出（便于阅读）
        Gson gson = new GsonBuilder()
                .setPrettyPrinting() // 格式化输出
                .serializeNulls()    // 空值也序列化（可选）
                .create();

        // 2. 转换为 JSON 字符串
        return gson.toJson(object);
    }

    public void updateBlockPositions(BlockPosition blockPosition){
        this.blockPositions = toJsonByGson(blockPosition);
    }

}
