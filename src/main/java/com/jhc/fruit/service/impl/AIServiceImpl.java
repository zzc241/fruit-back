package com.jhc.fruit.service.impl;

import com.alibaba.fastjson2.JSONArray;
import com.alibaba.fastjson2.JSONObject;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.jhc.fruit.common.HttpUtil;
import com.jhc.fruit.common.ImageDrawUtil;
import com.jhc.fruit.common.R;
import com.jhc.fruit.config.AIConfig;
import com.jhc.fruit.dao.AIDao;
import com.jhc.fruit.entity.AIWeeds;
import com.jhc.fruit.service.AIService;
import com.jhc.fruit.service.FileService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import java.util.ArrayList;
import java.util.List;

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
    private ImageDrawUtil imageDrawUtil;
    private static final String originPrefix = "AIWeed/origin";
    private static final String outputPrefix = "AIWeed/output";

    @Override
    public R<List<Integer>> AIOfRipeness(MultipartFile file, double conf, double iou) {
        //请求url
        String url = aiConfig.getAiCzm() + "/ripeness/detect/upload" + "?conf=" + conf + "&iou=" + iou;

        String response = httpUtil.doPostUpload(url, null, "file", file);
            //处理响应数据
        try {
            //校验响应是否成功
            if (response.startsWith("错误")) {
                String errorMsg = "AI接口响应失败，状态码：" + response;
                log.error(errorMsg);
                return R.error(errorMsg);
            }
            if (response == null || response.trim().isEmpty()) {
                log.error("AI接口响应体为空");
                return R.error("AI接口无响应数据");
            }
            log.info("AI接口返回原始JSON：{}", response);

            //解析JSON（容错处理）
            JSONObject res = JSONObject.parseObject(response);
            if (res == null) {
                log.error("JSON解析失败，原始数据：{}", response);
                return R.error("响应数据格式错误");
            }

            //获取成熟度数组并判空（核心业务逻辑）
            JSONArray ripenessDetections = res.getJSONArray("ripeness_detections");
            if (ripenessDetections == null || ripenessDetections.isEmpty()) {
                log.warn("ripeness_detections数组为空或不存在，完整JSON：{}", res.toJSONString());
                return R.error("未检测到果实成熟度信息");
            }

            //统计成熟果实数量（简化ripes列表，直接统计）
            List<String> ripes = new ArrayList<>();
            for (int i = 0; i < ripenessDetections.size(); i++) {
                JSONObject detection = ripenessDetections.getJSONObject(i);
                if (detection == null) {
                    log.warn("第{}条检测结果为null", i);
                    continue;
                }
                String ripenessName = detection.getString("ripeness_name");
                if (ripenessName != null && !ripenessName.isEmpty()) {
                    ripes.add(ripenessName);
                } else {
                    log.warn("第{}条检测结果无成熟度名称", i);
                }
            }

            //判断是否解析到有效数据
            if (ripes.isEmpty()) {
                log.warn("未解析到任何成熟度数据");
                return R.error("未检测到果实成熟度信息");
            }

            //统计成熟果实数量和总数
            Integer totalCount = ripes.size();
            Integer ripeCount = 0;
            for (String ripeness : ripes) {
                if ("Ripe".equals(ripeness)) {
                    ripeCount++;
                }
            }

            //封装结果并返回
            List<Integer> counts = new ArrayList<>();
            counts.add(ripeCount);
            counts.add(totalCount);
            return R.success(counts);

        } catch (Exception e) {
            // 统一捕获所有异常（工具类已处理IO异常，此处捕获解析/业务异常）
            log.error("AI识别异常", e);
            return R.error("系统异常: " + e.getMessage());
        }
    }

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
            //保存识别结果
            aiDao.save(detectionResponse.getResult().getDetections(), originUrl, outputUrl);

            return R.success(detectionResponse.getResult().getDetections());
        } catch (Exception e) {
            throw new RuntimeException("解析杂草检测结果失败", e);
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
}
