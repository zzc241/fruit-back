package com.jhc.fruit.service.impl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson2.JSONArray;
import com.alibaba.fastjson2.JSONException;
import com.alibaba.fastjson2.JSONObject;
import com.jhc.fruit.common.HttpUtil;
import com.jhc.fruit.common.R;
import com.jhc.fruit.config.AIConfig;
import com.jhc.fruit.service.AIService;
import lombok.extern.slf4j.Slf4j;
import okhttp3.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

@Slf4j
@Service
public class AIServiceImpl implements AIService {

    @Autowired
    private AIConfig aiConfig;
    private final HttpUtil httpUtil = HttpUtil.getInstance();

    @Override
    public R<List<Integer>> AIOfRipeness(MultipartFile file, double conf, double iou) {
        //创建请求对象
        String url = aiConfig.getEndpoint() + "/ripeness/detect/upload" + "?conf=" + conf + "&iou=" + iou;

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
}
