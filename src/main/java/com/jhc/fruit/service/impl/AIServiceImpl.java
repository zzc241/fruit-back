package com.jhc.fruit.service.impl;

import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.jhc.fruit.common.BaseContext;
import com.jhc.fruit.common.HttpUtil;
import com.jhc.fruit.common.ImageDrawUtil;
import com.jhc.fruit.common.R;
import com.jhc.fruit.config.AIConfig;
import com.jhc.fruit.dao.AIDao;
import com.jhc.fruit.entity.AIWeeds;
import com.jhc.fruit.entity.Disaster;
import com.jhc.fruit.entity.vo.DisasterVo;
import com.jhc.fruit.entity.vo.DiseaseResponse;
import com.jhc.fruit.entity.vo.MaturityResponse;
import com.jhc.fruit.service.AIService;
import com.jhc.fruit.service.DisasterService;
import com.jhc.fruit.service.FileService;
import lombok.extern.slf4j.Slf4j;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
        String url = "http://172.16.0.231:8000/api/weather-alert";
        
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
        // alerts.append("🚨 【低温冻害橙色预警】")
        // alerts.append("🚨 【高温热害红色预警】")
        // alerts.append("⚠️ 【高湿病害黄色预警】")
        // alerts.append("⚠️ 【干旱蓝色预警】")
        // alerts.append("⚠️ 【大风蓝色预警】")
        // alerts.append("✅ 【气象条件适宜】")
        
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
        disasterService.save(disaster);

        log.info(response);
        return disasterVo;
    }

    @Override
    public String chat(String query) {
        String url = "http://172.16.0.231:8000/api/chat";
        
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
        String url = "http://172.16.0.231:8000/api/analyze-water-fertilizer";
        
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

    public R<MaturityResponse> identifyMaturity(MultipartFile maturityFile) {
        String url = "http://172.16.0.231:8000/api/identify-maturity";
        
        // 构建请求头
        Map<String, String> headers = new HashMap<>();
        headers.put("accept", "application/json");
        
        // 构建文件参数
        Map<String, MultipartFile> fileParams = new HashMap<>();
        fileParams.put("maturityFile", maturityFile);  // 参数名根据接口文档调整
        
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
        String url = "http://172.16.0.231:8000/api/identify-disease";
        
        // 构建请求头
        Map<String, String> headers = new HashMap<>();
        headers.put("accept", "application/json");
        
        // 构建文件参数
        Map<String, MultipartFile> fileParams = new HashMap<>();
        fileParams.put("diseaseFile", diseaseFile);  // 参数名根据接口文档调整
        
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


}
