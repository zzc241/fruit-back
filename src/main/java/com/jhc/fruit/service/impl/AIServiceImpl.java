package com.jhc.fruit.service.impl;

import com.alibaba.fastjson2.JSONArray;
import com.alibaba.fastjson2.JSONException;
import com.alibaba.fastjson2.JSONObject;
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

    //创建客户端实例
    private final OkHttpClient client = new OkHttpClient().newBuilder()
            .connectTimeout(30, TimeUnit.SECONDS)  // 连接超时
            .readTimeout(30, TimeUnit.SECONDS)     // 读取超时
            .writeTimeout(30, TimeUnit.SECONDS)    // 写入超时
            .build();

    @Override
    public R<List<Integer>> AIOfRipeness(MultipartFile file, double conf, double iou) {
        List<String> ripes = new ArrayList<>();
        //创建请求对象
        String url = aiConfig.getEndpoint() + "/ripeness/detect/upload" + "?conf=" + conf + "&iou=" + iou;

        File tempFile = null;
        try {
            URL apiURL = new URL(url);
            //封装文件
            tempFile = File.createTempFile("okhttp-upload-", file.getOriginalFilename());
            //将文件内容写入临时文件
            file.transferTo(tempFile);

            MediaType mediaType = MediaType.parse(
                    file.getContentType() != null ? file.getContentType() : "application/octet-stream"
            );
            // 关键：文件上传必须用MultipartBody构建FORM格式请求体
            RequestBody requestBody = new MultipartBody.Builder()
                    .setType(MultipartBody.FORM) // 必须设置为FORM类型
                    .addFormDataPart(
                            "file",                // 文件参数名（AI接口要求的参数名）
                            file.getOriginalFilename(), // 文件名
                            RequestBody.create(tempFile, mediaType) // 文件体
                    )
                    .build();

            //请求参数
            Request request = new Request.Builder().url(apiURL).post(requestBody).build();
            //处理响应数据
            try (Response response = client.newCall(request).execute()) {
                // 第一步：校验响应是否成功
                if (!response.isSuccessful()) {
                    String errorMsg = "AI接口响应失败，状态码：" + response.code();
                    log.error(errorMsg);
                    return R.error(errorMsg);
                }

                // 第二步：安全读取响应体
                ResponseBody responseBody = response.body();
                if (responseBody == null) {
                    log.error("AI接口响应体为空");
                    return R.error("AI接口无响应数据");
                }
                String jsonResult = responseBody.string();
                log.info("AI接口返回原始JSON：{}", jsonResult);

                // 第三步：解析JSON（容错）
                JSONObject res = JSONObject.parseObject(jsonResult);
                if (res == null) {
                    log.error("JSON解析失败，原始数据：{}", jsonResult);
                    return R.error("响应数据格式错误");
                }

                // 第四步：获取数组并判空（核心修复：避免空指针）
                JSONArray ripenessDetections = res.getJSONArray("ripeness_detections");
                if (ripenessDetections != null) {
                    log.info("ripeness detections: " + ripenessDetections.toJSONString());
                    // 遍历赋值
                    if (!ripenessDetections.isEmpty()) {
                        for (int i = 0; i < ripenessDetections.size(); i++) {
                            JSONObject detection = ripenessDetections.getJSONObject(i);
                            if (detection != null) {
                                String ripenessName = detection.getString("ripeness_name");
                                if (ripenessName != null && !ripenessName.isEmpty()) {
                                    ripes.add(ripenessName);
                                } else {
                                    log.warn("第{}条检测结果无成熟度名称", i);
                                }
                            } else {
                                log.warn("第{}条检测结果为null", i);
                            }
                        }
                    } else {
                        log.warn("ripeness_detections数组为空");
                    }
                } else {
                    log.error("JSON中未找到ripeness_detections字段，完整JSON：{}", res.toJSONString());
                    return R.error("AI接口未返回成熟度数据");
                }
            }

            // ========== 修复点5：正确判断列表是否为空 ==========
            if (ripes.isEmpty()) {
                log.warn("未解析到任何成熟度数据");
                return R.error("未检测到果实成熟度信息");
            }

            Integer totalCount = ripes.size();
            Integer ripeCount = 0;
            for (int i = 0; i < ripes.size(); i++) {
                if(ripes.get(i).equals("Ripe")){
                    ripeCount += 1;
                }
            }
            List<Integer> counts = new ArrayList<>();
            counts.add(ripeCount);
            counts.add(totalCount);

            return R.success(counts);
        } catch (IOException e) {
            log.error("网络通信失败", e);
            return R.error("网络通信失败: " + e.getMessage());
        } catch (JSONException e) {
            log.error("JSON解析失败", e);
            return R.error("服务响应格式错误");
        } catch (Exception e) {
            log.error("AI识别异常", e);
            return R.error("系统异常: " + e.getMessage());
        }finally {
            //清理临时文件
            if (tempFile != null) {
                tempFile.delete();
            }
        }
    }
}
