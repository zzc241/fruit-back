package com.jhc.fruit.service.impl;

import com.alibaba.fastjson2.JSON;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.jhc.fruit.common.*;
import com.jhc.fruit.config.AIConfig;
import com.jhc.fruit.dao.AIDao;
import com.jhc.fruit.entity.*;
import com.jhc.fruit.entity.vo.DisasterVo;
import com.jhc.fruit.entity.vo.DiseaseResponse;
import com.jhc.fruit.entity.vo.MaturityResponse;
import com.jhc.fruit.service.AIService;
import com.jhc.fruit.service.DisasterService;
import com.jhc.fruit.service.FileService;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;

import okhttp3.*;
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
                outputUrl = "test.png";
            }
            //保存识别结果
            aiDao.save(detectionResponse.getResult().getDetections(), originUrl, outputUrl);

            return R.success(detectionResponse.getResult().getDetections());
        } catch (Exception e) {
            throw new RuntimeException("解析杂草检测结果失败", e);
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
        
        // 1. 构建请求头
        Map<String, String> headers = new HashMap<>();
        headers.put("accept", "application/json");
        
        // 2. 构建文件参数映射
        Map<String, MultipartFile> fileParams = new HashMap<>();
        fileParams.put("ms_file", msFile);
        fileParams.put("rgb_file", rgbFile);
        
        // 3. 调用多文件上传方法
        String response = httpUtil.doPostUploadMulti(url, headers, fileParams);
        
        log.info(response);
        return response;
    }

    @Override
    public String unanalyzeWaterFertilizer(String msFileName, String rgbFileName) {
        MultipartFile msFile = fileService.getMinioFileAsMultipartFile(msFileName);
        MultipartFile rgbFile = fileService.getMinioFileAsMultipartFile(rgbFileName);
        String url = aiConfig.getAiCzm() + "/api/analyze-water-fertilizer";

        // 1. 构建请求头
        Map<String, String> headers = new HashMap<>();
        headers.put("accept", "application/json");

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
        // 1. 原有校验逻辑（保留）
        if (images == null || images.size() != 4) {
            throw new IllegalArgumentException("必须上传且仅上传4张多光谱图片，当前数量：" + (images == null ? 0 : images.size()));
        }
        for (int i = 0; i < images.size(); i++) {
            MultipartFile image = images.get(i);
            if (image == null || image.isEmpty()) {
                throw new IllegalArgumentException("第" + (i+1) + "张图片为空，请检查");
            }
        }

        // 2. 构建第三方接口地址（保留）
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

        // 5. 调用第三方接口（保留）
        Map<String, String> headers = new HashMap<>();
        String responseBody = httpUtil.doPostUploadMulti(THIRD_PARTY_API_URL, headers, fileParams);

        // 6. 校验响应（保留）
        if (responseBody == null || responseBody.isEmpty()) {
            throw new RuntimeException("第三方接口返回空响应");
        }
        if (responseBody.startsWith("错误：")) {
            throw new RuntimeException("第三方接口调用失败：" + responseBody);
        }

        // 7. 解析响应（保留）
        MultiSpectralResponse spectralResponse;
        try {
            spectralResponse = objectMapper.readValue(responseBody, MultiSpectralResponse.class);
        } catch (Exception e) {
            throw new RuntimeException("解析响应失败，响应体：" + responseBody, e);
        }
        if (spectralResponse == null || spectralResponse.getCode() != 200) {
            throw new RuntimeException("分析失败，返回码：" + (spectralResponse == null ? "空" : spectralResponse.getCode()));
        }

        // 8. 提取核心数据
        MultiSpectralResponse.PythonResponseData pythonData = spectralResponse.getData();
        if (pythonData == null) {
            throw new RuntimeException("返回data字段为空");
        }
        MultiSpectralResponse.PythonFertilizerAnalysis analysis = pythonData.getJson_content();
        if (analysis == null) {
            throw new RuntimeException("返回json_content字段为空");
        }

        // ========== 核心修改：赋值所有字段（兜底默认值） ==========
        // 8.1 赋值光谱指数（Python返回null则用0.0兜底）
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

        // 8.2 赋值区块信息（地理+编码，null则用默认值兜底）
        BlockPosition blockPosition = new BlockPosition();
        blockPosition.setFile_name(pythonData.getJson_file_path());
        blockPosition.setLatitude(analysis.getLatitude() != null ? analysis.getLatitude() : 0.0);
        blockPosition.setLongitude(analysis.getLongitude() != null ? analysis.getLongitude() : 0.0);
        blockPosition.setAltitude(analysis.getAltitude() != null ? analysis.getAltitude() : 0.0);
        blockPosition.setFull_block_code(analysis.getFullBlockCode() != null ? analysis.getFullBlockCode() : "default");
        blockPosition.setBig_block(analysis.getBigBlock() != null ? analysis.getBigBlock() : "default");
        blockPosition.setSub_block(analysis.getSubBlock() != null ? analysis.getSubBlock() : "default");

        // 8.3 组装返回数据
        List<BlockPosition> blockPositions = new ArrayList<>();
        blockPositions.add(blockPosition);

        ApiResponse.ResponseData responseData = new ApiResponse.ResponseData();
        responseData.setNutritionStatus(analysis.getFertilizer_level() != null ? analysis.getFertilizer_level() : "normal");
        responseData.setBlockPositions(blockPositions);
        responseData.setSpectralIndices(spectralIndices);
        responseData.setAnalysisImage(null); // 暂无分析图片，保留null

        // 9. 组装最终响应
        ApiResponse apiResponse = new ApiResponse();
        apiResponse.setCode(200);
        apiResponse.setMsg("分析成功");
        apiResponse.setData(responseData);
        return apiResponse;
    }



}
