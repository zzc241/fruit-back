package com.jhc.fruit.entity.vo;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import com.jhc.fruit.entity.BlockPosition;
import com.jhc.fruit.entity.FullIndicesMean;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.Type;
import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;

@Data
public class AnalyzeVo {
    private Long orchardId;
    private Long areaId;
    private String analyzeSuggestion;
    private String nutritionStatus;
    private List<BlockPosition> blockPositions;
    private FullIndicesMean spectralIndices;
    private String analysisImage;
    private String mergeImage;
    @ApiModelProperty(value = "创建时间(自动填充)")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss.SSS", timezone = "GMT+8")
    private LocalDateTime createTime;

    // 新增日志记录器
    private static final Logger log = LoggerFactory.getLogger(AnalyzeVo.class);

    // 关键修改：Gson配置改为【大小写不敏感】+ 宽松模式
    private static final Gson GSON = new GsonBuilder()
            .serializeNulls()
            .setDateFormat("yyyy-MM-dd HH:mm:ss.SSS")
            .disableHtmlEscaping()
            .registerTypeAdapter(FullIndicesMean.class, (com.google.gson.JsonDeserializer<FullIndicesMean>) (json, typeOfT, context) -> {
                // 自定义反序列化：忽略字段大小写
                com.google.gson.JsonObject jsonObj = json.getAsJsonObject();
                FullIndicesMean mean = new FullIndicesMean();
                // 手动映射字段（解决大小写+特殊字段）
                mean.setNDVI(getDoubleValue(jsonObj, "NDVI", "ndvi"));
                mean.setNDRE(getDoubleValue(jsonObj, "NDRE", "ndre"));
                mean.setOSAVI(getDoubleValue(jsonObj, "OSAVI", "osavi"));
                mean.setNDPI(getDoubleValue(jsonObj, "NDPI", "ndpi"));
                mean.setKVI(getDoubleValue(jsonObj, "KVI", "kvi"));
                mean.setKSI(getDoubleValue(jsonObj, "KSI", "ksi"));
                mean.setTRI(getDoubleValue(jsonObj, "TRI", "tri"));
                mean.setMTCI(getDoubleValue(jsonObj, "MTCI", "mtci"));
                mean.setCaVI(getDoubleValue(jsonObj, "CaVI", "cavi", "cavi")); // 兼容CaVI/cavi/CAVI
                mean.setWI(getDoubleValue(jsonObj, "WI", "wi"));
                mean.setNDWI(getDoubleValue(jsonObj, "NDWI", "ndwi"));
                mean.setMSI(getDoubleValue(jsonObj, "MSI", "msi"));
                mean.setCI_rededge(getLongValue(jsonObj, "CI_rededge", "ci_rededge", "ciRededge"));
                mean.setCCI(getDoubleValue(jsonObj, "CCI", "cci"));
                mean.setPRI(getDoubleValue(jsonObj, "PRI", "pri"));
                mean.setSIPI(getDoubleValue(jsonObj, "SIPI", "sipi"));
                mean.setEVI(getDoubleValue(jsonObj, "EVI", "evi"));
                mean.setLAI(getDoubleValue(jsonObj, "LAI", "lai"));
                return mean;
            })
            .create();

    // 工具方法：获取Double值（兼容多字段名）
    private static Double getDoubleValue(com.google.gson.JsonObject jsonObj, String... fieldNames) {
        for (String field : fieldNames) {
            if (jsonObj.has(field) && !jsonObj.get(field).isJsonNull()) {
                try {
                    return jsonObj.get(field).getAsDouble();
                } catch (Exception e) {
                    log.warn("字段{}转换为Double失败", field, e);
                }
            }
        }
        return null;
    }

    // 工具方法：获取Long值（兼容多字段名 + JDK 17）
    private static Long getLongValue(com.google.gson.JsonObject jsonObj, String... fieldNames) {
        for (String field : fieldNames) {
            if (jsonObj.has(field) && !jsonObj.get(field).isJsonNull()) {
                try {
                    // 优先尝试直接获取Long
                    return jsonObj.get(field).getAsLong();
                } catch (com.google.gson.JsonSyntaxException e1) {
                    try {
                        // JDK 17适配：double基本类型转long（先转包装类再调用方法，或直接强转）
                        double doubleValue = jsonObj.get(field).getAsDouble();
                        // 方式1：转为Double包装类后调用longValue()（推荐，更安全）
                        Double doubleWrapper = doubleValue;
                        return doubleWrapper.longValue();
                        // 方式2：直接强转（简洁，效果相同）
                        // return (long) doubleValue;
                    } catch (com.google.gson.JsonSyntaxException e2) {
                        log.warn("字段{}既不是Long也不是Double，无法转换为Long", field, e2);
                    }
                }
            }
        }
        return null;
    }

    // BlockPosition转换方法（保留原有逻辑）
    public static List<BlockPosition> convertJsonToBlockPositionList(String jsonStr) {
        if (jsonStr == null || jsonStr.trim().isEmpty()) {
            return Collections.emptyList();
        }
        String trimJson = jsonStr.trim();
        try {
            if (trimJson.startsWith("[")) {
                Type listType = new TypeToken<List<BlockPosition>>() {}.getType();
                return GSON.fromJson(trimJson, listType);
            } else if (trimJson.startsWith("{")) {
                BlockPosition blockPosition = GSON.fromJson(trimJson, BlockPosition.class);
                return Collections.singletonList(blockPosition);
            } else {
                return Collections.emptyList();
            }
        } catch (Exception e) {
            log.error("BlockPosition转换失败", e);
            return Collections.emptyList();
        }
    }

    public void setBlockPositions(String blockPositions) {
        this.blockPositions = convertJsonToBlockPositionList(blockPositions);
    }

    // 核心修复：多光谱指数转换方法（增加原始值日志）
    public static FullIndicesMean convertJsonToSpectralIndices(String jsonStr) {
        // 打印原始JSON字符串，排查数据问题
        log.info("待转换的多光谱JSON字符串：{}", jsonStr);

        if (jsonStr == null || jsonStr.trim().isEmpty()) {
            log.warn("多光谱指数JSON字符串为空");
            return new FullIndicesMean(); // 返回空对象而非null，避免前端显示null
        }

        String trimJson = jsonStr.trim();
        try {
            if (trimJson.startsWith("{")) {
                FullIndicesMean mean = GSON.fromJson(trimJson, FullIndicesMean.class);
                log.info("多光谱指数转换结果：{}", mean);
                return mean;
            } else {
                log.error("多光谱JSON不是对象格式：{}", trimJson);
                return new FullIndicesMean();
            }
        } catch (Exception e) {
            log.error("多光谱指数JSON转换失败", e);
            return new FullIndicesMean();
        }
    }

    // 字符串入参的set方法
    public void setSpectralIndices(String spectralIndices) {
        this.spectralIndices = convertJsonToSpectralIndices(spectralIndices);
    }
}