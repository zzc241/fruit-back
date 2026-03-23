package com.jhc.fruit.service.impl;

import com.alibaba.fastjson2.JSON;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.jhc.fruit.common.*;
import com.jhc.fruit.config.AIConfig;
import com.jhc.fruit.dao.AIDao;
import com.jhc.fruit.entity.*;
import com.jhc.fruit.entity.vo.AnalyzeVo;
import com.jhc.fruit.entity.vo.DisasterVo;
import com.jhc.fruit.entity.vo.DiseaseResponse;
import com.jhc.fruit.entity.vo.MaturityResponse;
import com.jhc.fruit.service.AIService;
import com.jhc.fruit.service.DisasterService;
import com.jhc.fruit.service.FileService;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.*;

@Slf4j
@Service
public class AIServiceImpl extends ServiceImpl<AIDao, AIWeeds.WeedDetection> implements AIService {
    @Autowired
    private AIConfig aiConfig;
    @Autowired
    private FileService fileService;
    private final HttpUtil httpUtil = HttpUtil.getInstance();
    private final ObjectMapper objectMapper = new ObjectMapper();
    @Autowired
    private AIDao aiDao;
    @Autowired
    DisasterService disasterService;
    @Autowired
    private ImageDrawUtil imageDrawUtil;
    private static final String originPrefix = "AIWeed/origin";
    private static final String outputPrefix = "AIWeed/output";


    /**
     * 调用AI接口检测杂草
     * @param imageFile 待检测的图像文件
     * @return 解析后的检测结果
     */
    public R<List<AIWeeds.WeedDetection>> AIWeed(MultipartFile imageFile) {
        StringBuffer sb = new StringBuffer();
        sb.append(aiConfig.getAiZen());
        sb.append("/predict");
        String url = sb.toString();
        //保存原始图像
        String originUrl = fileService.save(imageFile, originPrefix);

        try {
            String responseData = httpUtil.doPostUpload(url, null, "file", imageFile);

            // 校验响应数据非空（避免解析空字符串报错）
            if (responseData == null || responseData.isEmpty()) {
                return R.error("检测失败：AI接口返回空结果");
            }

            // 解析JSON响应为目标对象
            AIWeeds.WeedDetectionResponse detectionResponse =
                    objectMapper.readValue(responseData, AIWeeds.WeedDetectionResponse.class);
            //处理图片
            String outputUrl = AIWeedImg(originUrl, outputPrefix, detectionResponse.getResult().getDetections());
            if (outputUrl.equals("err")) {
                outputUrl = "ERROR.png";
            }
            log.info("--test--:" + detectionResponse.getResult().getDetections());
            if (detectionResponse.getResult().getDetections() == null || detectionResponse.getResult().getDetections().isEmpty()) {
                return R.error("识别失败，请上传包含杂草的图片");
            }
            //保存识别结果
            aiDao.save(detectionResponse.getResult().getDetections(), originUrl, outputUrl);
            String finalOutputUrl = outputUrl;
            detectionResponse.getResult().getDetections().forEach(item ->{
                item.setOriginImg(fileService.getImageUrl(originUrl));
                item.setOutputImg(fileService.getImageUrl(finalOutputUrl));
            });

            log.info("处理完成，识别结果图片路径:" + outputUrl);

            return R.success(detectionResponse.getResult().getDetections());
        } catch (Exception e) {
            log.info("解析杂草检测结果失败:", e);
            return R.error("解析杂草检测结果失败");
        }
    }

    /**
     * 处理检测结果（核心业务逻辑）
     * @param response 接口返回的原始结果
     */
    public void processDetectionResult(AIWeeds.WeedDetectionResponse response) {
        // 校验接口调用是否成功
        if (response.getCode() != 200) {
            System.err.println("检测失败：" + response.getMessage());
            return;
        }

        AIWeeds.DetectionResult result = response.getResult();
        List<AIWeeds.WeedDetection> detections = result.getDetections();
        List<Integer> imageShape = result.getImageShape();
        int imageHeight = imageShape.get(0);
        int imageWidth = imageShape.get(1);

        // 1. 过滤低置信度结果（只保留置信度≥0.7的杂草）
        List<AIWeeds.WeedDetection> validDetections = detections.stream()
                .filter(d -> d.getConfidence() >= 0.7)
                .toList();

        // 2. 统计杂草信息
        System.out.println("有效杂草数量：" + validDetections.size());
        for (AIWeeds.WeedDetection detection : validDetections) {
            // 计算杂草区域面积（像素）
            double width = detection.getX2() - detection.getX1();
            double height = detection.getY2() - detection.getY1();
            double area = width * height;

            // 输出单株杂草信息
            System.out.println("------------------------");
            System.out.println("杂草种类：" + detection.getClassName());
            System.out.println("置信度：" + String.format("%.2f%%", detection.getConfidence() * 100));
            System.out.println("位置坐标：(" + detection.getX1() + "," + detection.getY1() + ") - (" + detection.getX2() + "," + detection.getY2() + ")");
            System.out.println("覆盖面积：" + String.format("%.2f像素", area));
            System.out.println("占图像比例：" + String.format("%.2f%%", (area / (imageHeight * imageWidth)) * 100));
        }
    }

    @Override
    public DisasterVo weatherAlert(Long orchardId) {
        String city = aiDao.getCityByOrchardId(orchardId);
        // 1. 构建请求URL
        String url = aiConfig.getAiCzm() + "/api/weather-alert";
        
        // 2. 构建请求参数
        Map<String, String> params = new HashMap<>();
        params.put("city", city);
        
        // 3. 构建请求头（可选，根据接口要求）
        Map<String, String> headers = new HashMap<>();
        headers.put("accept", "application/json");
        
        // 4. 调用 GET 请求
        String response = httpUtil.doGet(url, headers, params);

        // 5. 解析JSON响应
        // JSONObject jsonResponse = JSONObject.parseObject(response);
        // JSONObject data = jsonResponse.getJSONObject("data");
        // String content = data.getString("content");

        String content = response;  // response 已经是纯文本内容
        String disasterType = "";
        String adviceContent = "";
    
        // 从content中提取预警信息
        if (content.contains("干旱蓝色预警")) {
            disasterType = "干旱蓝色预警";
        } else if (content.contains("大风蓝色预警")) {
            disasterType = "大风蓝色预警";
        } else if (content.contains("高湿病害黄色预警")){
            disasterType = "高湿病害黄色预警";
        } else if ( content.contains("高温热害红色预警")){
            disasterType = "高温热害红色预警";
        } else if (content.contains("低温冻害橙色预警")){
            disasterType = "低温冻害橙色预警";
        } else {
            disasterType = "气象条件适宜";
        }
        // 可以根据需要添加更多预警类型
        // alerts.append(" 【低温冻害橙色预警】")
        // alerts.append(" 【高温热害红色预警】")
        // alerts.append(" 【高湿病害黄色预警】")
        // alerts.append(" 【干旱蓝色预警】")
        // alerts.append(" 【大风蓝色预警】")
        // alerts.append(" 【气象条件适宜】")
        
        // 提取管理建议部分
        if (content.contains("柑橘种植管理建议")) {
            int startIndex = content.indexOf("柑橘种植管理建议");
            adviceContent = content.substring(startIndex);
        } else {
            adviceContent = content; // 如果没有管理建议，保存完整内容
        }
        
        // 5. 解析响应并返回 Disaster 对象
        DisasterVo disasterVo = new DisasterVo();
        disasterVo.setOrchardId(orchardId);
        disasterVo.setContent(adviceContent);
        disasterVo.setUserId(BaseContext.getId());
        disasterVo.setCreateTime(LocalDateTime.now());
        disasterVo.setDisasterType(disasterType);
        Disaster disaster = new Disaster();
        BeanUtils.copyProperties(disasterVo, disaster);
        disaster.setTypeId(1L);
        disasterService.save(disaster);

        log.info(response);
        return disasterVo;
    }

    @Override
    public String chat(String query) {
        String url = aiConfig.getAiCzm() + "/api/chat";
        
        // 1. 构建请求体参数（POST 请求的参数放在 body 中）
        Map<String, Object> bodyParams = new HashMap<>();
        bodyParams.put("query", query);
        
        // 2. 构建请求头
        Map<String, String> headers = new HashMap<>();
        headers.put("accept", "application/json");
        headers.put("Content-Type", "application/json");  // 必须添加
        
        // 3. 调用 POST 请求（使用 doPost 方法）
        String response = httpUtil.doPost(url, headers, "json", bodyParams);
        
        // 4. 直接返回响应内容
        return response;
    }

    public String analyzeWaterFertilizer(MultipartFile msFile, MultipartFile rgbFile) {
        String url = aiConfig.getAiCzm() + "/api/analyze-water-fertilizer";

        // 添加文件验证
        if (msFile == null || msFile.isEmpty()) {
            log.error("MS文件为空或不存在: ");
            return "错误：MS文件获取失败";
        }
        if (rgbFile == null || rgbFile.isEmpty()) {
            log.error("RGB文件为空或不存在: ");
            return "错误：RGB文件获取失败";
        }
        // 1. 构建请求头
        Map<String, String> headers = new HashMap<>();
        headers.put("accept", "multipart/form-data");
        
        // 2. 构建文件参数映射
        Map<String, MultipartFile> fileParams = new HashMap<>();
        fileParams.put("ms_file", msFile);
        fileParams.put("rgb_file", rgbFile);
        
        // 3. 调用多文件上传方法
        String response = httpUtil.doPostUploadMulti(url, headers, fileParams);
        
        log.info(response);
        String responseResult = extractResult(response);
        return responseResult;
    }

    /**
     * 提取水肥分析结果的result文本
     * @param jsonResponse 从水肥分析服务返回的JSON
     * @return result文本内容
     */
    private String extractResult(String jsonResponse) {
        try {
            log.info("开始解析水肥分析结果: {}", jsonResponse);

            // 直接解析JSON（现在是单层结构）
            JsonObject root = JsonParser.parseString(jsonResponse).getAsJsonObject();

            // 检查返回码
            int code = root.get("code").getAsInt();
            if (code != 200) {
                String msg = root.has("msg") ? root.get("msg").getAsString() : "未知错误";
                log.error("水肥分析服务返回错误: code={}, msg={}", code, msg);
                return "水肥分析失败：" + msg;
            }

            // 检查并获取result字段
            if (!root.has("result")) {
                log.error("返回结果中不存在result字段");
                return null;
            }

            String result = root.get("result").getAsString();
            log.info("成功提取result，长度: {}", result.length());

            return result;

        } catch (Exception e) {
            log.error("解析水肥分析结果失败: {}", e.getMessage(), e);
            return null;
        }
    }

    @Override
    public String unanalyzeWaterFertilizer(String msFileName, String rgbFileName) {
        MultipartFile msFile = fileService.getMinioFileAsMultipartFile(msFileName);
        MultipartFile rgbFile = fileService.getMinioFileAsMultipartFile(rgbFileName);
        String url = aiConfig.getAiCzm() + "/api/analyze-water-fertilizer";

        // 添加文件验证
        if (msFile == null || msFile.isEmpty()) {
            log.error("MS文件为空或不存在: {}", msFileName);
            return "错误：MS文件获取失败";
        }
        if (rgbFile == null || rgbFile.isEmpty()) {
            log.error("RGB文件为空或不存在: {}", rgbFileName);
            return "错误：RGB文件获取失败";
        }
        // 1. 构建请求头
        Map<String, String> headers = new HashMap<>();
        headers.put("accept", "multipart/form-data");

        // 2. 构建文件参数映射
        Map<String, MultipartFile> fileParams = new HashMap<>();
        fileParams.put("ms_file", msFile);
        fileParams.put("rgb_file", rgbFile);

        // 3. 调用多文件上传方法
        String response = httpUtil.doPostUploadMulti(url, headers, fileParams);

        log.info(response);
        return response;
    }

    /**
     * Flask端音频处理失败响应的内部类
     */
    @Data
    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class AudioRecognitionError {
        private String error; // Flask返回的error字段
    }

    // ========== 修改后的processAudio方法 ==========
    @Override
    public R<AudioRecognitionSuccess> processAudio(MultipartFile wavFile) {
        StringBuffer sb = new StringBuffer();
        sb.append(aiConfig.getAiAudio());
        sb.append("/process_audio"); // 路径已修复为下划线
        String url = sb.toString();

        // 初始化ObjectMapper（建议注入全局实例，此处简化）
        ObjectMapper objectMapper = new ObjectMapper();

        try {
            // 1. 调用工具类上传文件（参数名改为audio，匹配Flask）
            String responseData = httpUtil.doPostUpload(url, null, "audio", wavFile);

            // 2. 校验响应非空
            if (responseData == null || responseData.isEmpty()) {
                return R.error("检测失败：AI接口返回空结果");
            }

            // 3. 先尝试解析为成功响应
            try {
                AudioRecognitionSuccess successResp = objectMapper.readValue(responseData, AudioRecognitionSuccess.class);
                if (successResp.isSuccess()) {
                    return R.success(successResp);
                }
            } catch (JsonProcessingException e) {
                // 不是成功响应，尝试解析为失败响应
                try {
                    AudioRecognitionError errorResp = objectMapper.readValue(responseData, AudioRecognitionError.class);
                    return R.error("AI接口处理失败：" + errorResp.getError());
                } catch (JsonProcessingException ex) {
                    // 既不是成功也不是失败格式，返回原始错误
                    return R.error("AI接口响应格式异常：" + responseData);
                }
            }

            // 兜底返回
            return R.error("AI接口响应解析失败");

        } catch (Exception e) {
            log.error("音频处理失败", e); // 打印完整堆栈
            return R.error("WAV接口报错：" + e.getMessage());
        }
    }

    public R<MaturityResponse> identifyMaturity(MultipartFile maturityFile) {
        String url = aiConfig.getAiCzm() + "/api/identify-maturity";
        
        // 构建请求头
        Map<String, String> headers = new HashMap<>();
        headers.put("accept", "application/json");
        
        // 构建文件参数
        Map<String, MultipartFile> fileParams = new HashMap<>();
        fileParams.put("file", maturityFile);  // 参数名根据接口文档调整
        
        // 调用接口
        String response = httpUtil.doPostUploadMulti(url, headers, fileParams);
        
        if (response.startsWith("错误：")) {
            return R.error(response);
        }
        
        try {
            // 解析JSON响应为MaturityResponse对象
            MaturityResponse maturityResponse = JSON.parseObject(response, MaturityResponse.class);
            return R.success(maturityResponse);
        } catch (Exception e) {
            log.error("解析成熟度响应失败", e);
            return R.error("解析响应失败");
        }
    }
    public R<DiseaseResponse> identifyDisease(MultipartFile diseaseFile) {
        String url = aiConfig.getAiCzm() + "/api/identify-disease";
        
        // 构建请求头
        Map<String, String> headers = new HashMap<>();
        headers.put("accept", "application/json");
        
        // 构建文件参数
        Map<String, MultipartFile> fileParams = new HashMap<>();
        fileParams.put("file", diseaseFile);  // 参数名根据接口文档调整
        
        // 调用接口
        String response = httpUtil.doPostUploadMulti(url, headers, fileParams);
        
        if (response.startsWith("错误：")) {
            return R.error(response);
        }
        
        try {
            // 解析JSON响应为DiseaseResponse对象
            DiseaseResponse diseaseResponse = JSON.parseObject(response, DiseaseResponse.class);
            return R.success(diseaseResponse);
        } catch (Exception e) {
            log.error("解析病虫害响应失败", e);
            return R.error("解析响应失败");
        }
    }

    /**
     * 根据返回坐标对图片打框
     * @return 图片访问url
     * @param originalMinioPath 原始图片路径
     * @param outputPrefix 处理图片要上传的路径
     * @param detections ai接口返回数据
     * */
    @Override
    public String AIWeedImg(String originalMinioPath,
                            String outputPrefix,
                            List<AIWeeds.WeedDetection> detections) {
        try {
            //画框并上传到minio
            String outputUrl = imageDrawUtil.drawWeedBoxesAndUploadToMinio(originalMinioPath, outputPrefix, detections);
            return outputUrl;
        }catch (Exception e) {
            log.info("处理失败:{}", e.getMessage());
            return "err";
        }
    }

    @Override
    public ApiResponse analyzeMultiSpectralImages(List<MultipartFile> images) throws IOException {
        // 1. 校验逻辑
        if (images == null || images.size() != 4) {
            throw new IllegalArgumentException("必须上传且仅上传4张多光谱图片，当前数量：" + (images == null ? 0 : images.size()));
        }
        for (int i = 0; i < images.size(); i++) {
            MultipartFile image = images.get(i);
            if (image == null || image.isEmpty()) {
                throw new IllegalArgumentException("第" + (i + 1) + "张图片为空，请检查");
            }
        }

        // 2. 构建第三方接口地址
        String THIRD_PARTY_API_URL = aiConfig.getAiCbh() + "/analyze";
        if (THIRD_PARTY_API_URL == null || THIRD_PARTY_API_URL.trim().isEmpty()) {
            throw new RuntimeException("第三方接口地址为空，请检查AIConfig配置");
        }

        // 3. 映射文件参数（保留）
        Map<String, MultipartFile> fileParams = new HashMap<>();
        fileParams.put("nm555", images.get(0));
        fileParams.put("nm660", images.get(1));
        fileParams.put("nm720", images.get(2));
        fileParams.put("nm840", images.get(3));

        // 4. 校验文件格式（保留）
        for (Map.Entry<String, MultipartFile> entry : fileParams.entrySet()) {
            String paramName = entry.getKey();
            MultipartFile file = entry.getValue();
            String fileName = file.getOriginalFilename();
            if (fileName == null || !fileName.endsWith(".tif")) {
                throw new IllegalArgumentException("参数" + paramName + "格式错误，仅支持.tif，当前：" + fileName);
            }
        }

        // 5. 调用工具类
        Map<String, String> headers = new HashMap<>();
        String responseBody = httpUtil.doPostUploadMulti(THIRD_PARTY_API_URL, headers, fileParams);

        // 6. 校验响应
        if (responseBody == null || responseBody.isEmpty()) {
            throw new RuntimeException("第三方接口返回空响应");
        }
        if (responseBody.startsWith("错误：")) {
            throw new RuntimeException("第三方接口调用失败：" + responseBody);
        }

        // ==================== 7. 解析响应（已修复） ====================
        MultiSpectralResponse spectralResponse;
        MultiSpectralResponse.PythonFertilizerAnalysis analysis = null;
        MultiSpectralResponse.PythonResponseData pythonData = null;
        String photoPrefix = null;
        String imageUrl = null;  // 新增：保存图片URL
        String actualFileName = null;  // 新增：保存实际文件名
        String mergeUrl = null; //合并后的多光谱图片url

        try {
            JsonNode rootNode = objectMapper.readTree(responseBody);
            log.info("成功接收到响应，开始解析");

            // 检查响应格式
            if (rootNode.has("code") && rootNode.get("code").asInt() == 200) {
                // 新格式：包含code, msg, data字段
                JsonNode dataNode = rootNode.get("data");
                log.info(dataNode.toString());

                // 从data中获取照片编号和图片URL
                if (dataNode.has("照片编号")) {
                    photoPrefix = dataNode.get("照片编号").asText();
                    log.info("照片编号: {}", photoPrefix);
                }

                // 获取图片URL（如果有）
                if (dataNode.has("image_url") && !dataNode.get("image_url").isNull()) {
                    imageUrl = dataNode.get("image_url").asText();
                    log.info("图片URL: {}", imageUrl);
                }

                //获取合并后图片url
                if (dataNode.has("merge_url") && !dataNode.get("merge_url").isNull()) {
                    mergeUrl = dataNode.get("merge_url").asText();
                    log.info("合成图URL: {}", mergeUrl);
                }

                // 获取实际的分析数据 - 注意字段名是中文的
                JsonNode analysisDataNode = dataNode.get("fertilizer_analysis_json全部数据");
                if (analysisDataNode != null) {
                    log.info("找到分析数据节点");

                    // 手动构建响应对象
                    spectralResponse = new MultiSpectralResponse();
                    spectralResponse.setCode(200);
                    spectralResponse.setMsg("success");

                    // 构建PythonResponseData
                    pythonData = new MultiSpectralResponse.PythonResponseData();
                    pythonData.setJson_file_path(getStringValue(analysisDataNode, "visual_save_path"));

                    // 从block_position获取实际文件名（修复关键点）
                    JsonNode blockNode = analysisDataNode.get("block_position");
                    if (blockNode != null && blockNode.isArray() && blockNode.size() > 0) {
                        JsonNode firstBlock = blockNode.get(0);
                        actualFileName = getStringValue(firstBlock, "file_name");
                        log.info("实际文件名: {}", actualFileName);
                    }

                    // 构建PythonFertilizerAnalysis
                    analysis = extractAnalysisFromJson(analysisDataNode);

                    pythonData.setJson_content(analysis);
                    spectralResponse.setData(pythonData);

                    log.info("数据解析成功");
                } else {
                    log.error("未找到fertilizer_analysis_json全部数据节点");
                    throw new RuntimeException("无法找到分析数据");
                }
            } else {
                // 旧格式兼容
                log.warn("使用旧格式解析");
                photoPrefix = rootNode.fieldNames().next();
                JsonNode actualData = rootNode.get(photoPrefix);

                spectralResponse = new MultiSpectralResponse();
                spectralResponse.setCode(200);
                spectralResponse.setMsg("success");

                pythonData = new MultiSpectralResponse.PythonResponseData();
                pythonData.setJson_file_path(getStringValue(actualData, "visual_save_path"));

                // 从block_position获取实际文件名
                JsonNode blockNode = actualData.get("block_position");
                if (blockNode != null && blockNode.isArray() && blockNode.size() > 0) {
                    JsonNode firstBlock = blockNode.get(0);
                    actualFileName = getStringValue(firstBlock, "file_name");
                }

                analysis = extractAnalysisFromJson(actualData);

                pythonData.setJson_content(analysis);
                spectralResponse.setData(pythonData);
            }

            log.info("成功解析响应，照片编号: {}", photoPrefix);

        } catch (Exception e) {
            log.error("解析响应失败，原始响应: {}", responseBody, e);
            throw new RuntimeException("解析响应失败: " + e.getMessage(), e);
        }

        // 8. 验证解析结果
        if (analysis == null) {
            throw new RuntimeException("解析后的analysis对象为空");
        }

        // ==================== 9. 赋值光谱指数 ====================
        FullIndicesMean spectralIndices = new FullIndicesMean();
        spectralIndices.setNDVI(analysis.getNdvi() != null ? analysis.getNdvi() : 0.0);
        spectralIndices.setNDRE(analysis.getNdre() != null ? analysis.getNdre() : 0.0);
        spectralIndices.setOSAVI(analysis.getOsavi() != null ? analysis.getOsavi() : 0.0);
        spectralIndices.setNDPI(analysis.getNdpi() != null ? analysis.getNdpi() : 0.0);
        spectralIndices.setKVI(analysis.getKvi() != null ? analysis.getKvi() : 0.0);
        spectralIndices.setKSI(analysis.getKsi() != null ? analysis.getKsi() : 0.0);
        spectralIndices.setTRI(analysis.getTri() != null ? analysis.getTri() : 0.0);
        spectralIndices.setMTCI(analysis.getMtci() != null ? analysis.getMtci() : 0.0);
        spectralIndices.setCaVI(analysis.getCavi() != null ? analysis.getCavi() : 0.0);
        spectralIndices.setWI(analysis.getWi() != null ? analysis.getWi() : 0.0);
        spectralIndices.setNDWI(analysis.getNdwi() != null ? analysis.getNdwi() : 0.0);
        spectralIndices.setMSI(analysis.getMsi() != null ? analysis.getMsi() : 0.0);
        spectralIndices.setCI_rededge(analysis.getCiRededge() != null ? analysis.getCiRededge() : 0L);
        spectralIndices.setCCI(analysis.getCci() != null ? analysis.getCci() : 0.0);
        spectralIndices.setPRI(analysis.getPri() != null ? analysis.getPri() : 0.0);
        spectralIndices.setSIPI(analysis.getSipi() != null ? analysis.getSipi() : 0.0);
        spectralIndices.setEVI(analysis.getEvi() != null ? analysis.getEvi() : 0.0);
        spectralIndices.setLAI(analysis.getLai() != null ? analysis.getLai() : 0.0);

        // ==================== 10. 赋值区块信息（修复文件名问题） ====================
        BlockPosition blockPosition = new BlockPosition();
        // 修复：使用实际的文件名，而不是保存路径
        if (actualFileName != null) {
            blockPosition.setFile_name(actualFileName);
        } else {
            // 如果获取失败，尝试从第一个文件名获取
            try {
                JsonNode rootNode = objectMapper.readTree(responseBody);
                JsonNode dataNode = rootNode.get("data");
                JsonNode analysisDataNode = dataNode.get("fertilizer_analysis_json全部数据");
                JsonNode blockNode = analysisDataNode.get("block_position");
                if (blockNode != null && blockNode.isArray() && blockNode.size() > 0) {
                    JsonNode firstBlock = blockNode.get(0);
                    blockPosition.setFile_name(getStringValue(firstBlock, "file_name"));
                } else {
                    blockPosition.setFile_name("unknown.tif");
                }
            } catch (Exception e) {
                log.warn("获取文件名失败，使用默认值", e);
                blockPosition.setFile_name("unknown.tif");
            }
        }

        blockPosition.setLatitude(analysis.getLatitude() != null ? analysis.getLatitude() : 0.0);
        blockPosition.setLongitude(analysis.getLongitude() != null ? analysis.getLongitude() : 0.0);
        blockPosition.setAltitude(analysis.getAltitude() != null ? analysis.getAltitude() : 0.0);
        blockPosition.setFull_block_code(analysis.getFullBlockCode() != null ? analysis.getFullBlockCode() : "default");
        blockPosition.setBig_block(analysis.getBigBlock() != null ? analysis.getBigBlock() : "default");
        blockPosition.setSub_block(analysis.getSubBlock() != null ? analysis.getSubBlock() : "default");

        // ==================== 11. 组装返回数据 ====================
        List<BlockPosition> blockPositions = new ArrayList<>();
        blockPositions.add(blockPosition);

        ApiResponse.ResponseData responseData = new ApiResponse.ResponseData();
        responseData.setNutritionStatus(analysis.getFertilizer_level() != null ? analysis.getFertilizer_level() : "normal");
        responseData.setBlockPositions(blockPositions);
        responseData.setSpectralIndices(spectralIndices);

        responseData.setAnalysisImage(imageUrl);
        responseData.setMergeImage(mergeUrl);

        // 12. 组装最终响应
        ApiResponse apiResponse = new ApiResponse();
        apiResponse.setCode(200);
        apiResponse.setMsg("分析成功");
        apiResponse.setData(responseData);

        log.info("分析完成，照片编号: {}, 营养状态: {}", photoPrefix, analysis.getFertilizer_level());
        return apiResponse;
    }

    @Override
    public ApiResponse analyzeMultiSpectralImagesPlus(List<MultipartFile> images, Long orchardId, Long areaId) {
        ApiResponse apiResponse = new ApiResponse();
        try {
            //获取rgb图片
            MultipartFile rgbFile = images.remove(4);
            //剩下4张tif用于分析
            apiResponse = analyzeMultiSpectralImages(images);
            MultipartFile msFile = fileService.getMinioFileAsMultipartFile(
                    apiResponse.getData().getMergeImage());

            apiResponse.getData().setAnalyzeSuggestion(analyzeWaterFertilizer(msFile , rgbFile));
            //保存数据
            Analyze analyze = new Analyze();
            analyze.setAreaId(areaId);
            analyze.setOrchardId(orchardId);
            analyze.setAnalyzeSuggestion(apiResponse.getData().getAnalyzeSuggestion());
            analyze.setMergeImage(apiResponse.getData().getMergeImage());
            analyze.setAnalysisImage(apiResponse.getData().getAnalysisImage());
            analyze.setNutritionStatus(apiResponse.getData().getNutritionStatus());
            analyze.updateSpectralIndices(apiResponse.getData().getSpectralIndices());
            analyze.updateBlockPositions(apiResponse.getData().getBlockPositions().get(0));

            aiDao.saveAnalyze(analyze);
        }catch (Exception e){
            throw new RuntimeException(e);
        }
        return apiResponse;
    }

    @Override
    public R<List<AnalyzeVo>> getAnalyze(Long orchardId, Long areaId) {
        List<Analyze> analyzeList = aiDao.getAnalyze(orchardId,areaId);
        log.info(analyzeList.toString());
        List<AnalyzeVo> voList = new ArrayList<>();
        for (Analyze analyze : analyzeList) {
            AnalyzeVo vo = new AnalyzeVo();
            vo.setAreaId(analyze.getAreaId());
            vo.setOrchardId(orchardId);
            vo.setAnalysisImage(fileService.getImageUrl(analyze.getAnalysisImage()));
            vo.setNutritionStatus(analyze.getNutritionStatus());
            vo.setAnalyzeSuggestion(analyze.getAnalyzeSuggestion());
            vo.setMergeImage(fileService.getImageUrl(analyze.getMergeImage()));
            vo.setNutritionStatus(analyze.getNutritionStatus());
            vo.setBlockPositions(analyze.getBlockPositions());
            vo.setSpectralIndices(analyze.getSpectralIndices());
            voList.add(vo);
        }
        return R.success(voList);
    }

    // 辅助方法：安全的获取double值
    private Double getDoubleValue(JsonNode node, String field) {
        if (node.has(field) && !node.get(field).isNull()) {
            return node.get(field).asDouble();
        }
        return null;
    }

    // 辅助方法：安全的获取long值
    private Long getLongValue(JsonNode node, String field) {
        if (node.has(field) && !node.get(field).isNull()) {
            return node.get(field).asLong();
        }
        return null;
    }

    // 辅助方法：安全的获取string值
    private String getStringValue(JsonNode node, String field) {
        if (node.has(field) && !node.get(field).isNull()) {
            return node.get(field).asText();
        }
        return null;
    }

    // 从状态文本解析肥料等级
    private String parseFertilizerLevel(String statusText) {
        if (statusText == null) return "normal";

        if (statusText.contains("氮素偏少") || statusText.contains("磷素缺乏")) {
            return "deficiency";
        } else if (statusText.contains("过量")) {
            return "excess";
        } else {
            return "normal";
        }
    }

    // 新增方法：从JSON节点提取分析数据
    private MultiSpectralResponse.PythonFertilizerAnalysis extractAnalysisFromJson(JsonNode dataNode) {
        MultiSpectralResponse.PythonFertilizerAnalysis analysis =
                new MultiSpectralResponse.PythonFertilizerAnalysis();

        // 从 full_indices_mean 中提取光谱指数
        JsonNode indicesNode = dataNode.get("full_indices_mean");
        if (indicesNode != null) {
            analysis.setNdvi(getDoubleValue(indicesNode, "NDVI"));
            analysis.setNdre(getDoubleValue(indicesNode, "NDRE"));
            analysis.setOsavi(getDoubleValue(indicesNode, "OSAVI"));
            analysis.setNdpi(getDoubleValue(indicesNode, "NDPI"));
            analysis.setKvi(getDoubleValue(indicesNode, "KVI"));
            analysis.setKsi(getDoubleValue(indicesNode, "KSI"));
            analysis.setTri(getDoubleValue(indicesNode, "TRI"));
            analysis.setMtci(getDoubleValue(indicesNode, "MTCI"));
            analysis.setCavi(getDoubleValue(indicesNode, "CaVI"));
            analysis.setWi(getDoubleValue(indicesNode, "WI"));
            analysis.setNdwi(getDoubleValue(indicesNode, "NDWI"));
            analysis.setMsi(getDoubleValue(indicesNode, "MSI"));
            analysis.setCiRededge(getLongValue(indicesNode, "CI_rededge"));
            analysis.setCci(getDoubleValue(indicesNode, "CCI"));
            analysis.setPri(getDoubleValue(indicesNode, "PRI"));
            analysis.setSipi(getDoubleValue(indicesNode, "SIPI"));
            analysis.setEvi(getDoubleValue(indicesNode, "EVI"));
            analysis.setLai(getDoubleValue(indicesNode, "LAI"));
        }

        // 从 block_position 提取区块信息
        JsonNode blockNode = dataNode.get("block_position");
        if (blockNode != null && blockNode.isArray() && blockNode.size() > 0) {
            JsonNode firstBlock = blockNode.get(0);
            analysis.setLatitude(getDoubleValue(firstBlock, "latitude"));
            analysis.setLongitude(getDoubleValue(firstBlock, "longitude"));
            analysis.setAltitude(getDoubleValue(firstBlock, "altitude"));
            analysis.setFullBlockCode(getStringValue(firstBlock, "full_block_code"));
            analysis.setBigBlock(getStringValue(firstBlock, "big_block"));
            analysis.setSubBlock(getStringValue(firstBlock, "sub_block"));
        }

        // 从 full_status_text 提取肥料等级
        String statusText = getStringValue(dataNode, "full_status_text");
        analysis.setFertilizer_level(parseFertilizerLevel(statusText));

        return analysis;
    }



}
