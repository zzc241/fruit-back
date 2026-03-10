package com.jhc.fruit.common;

import com.jhc.fruit.service.FileService;
import okhttp3.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.Map;
import java.util.concurrent.TimeUnit;

@Component
public class HttpUtil {

    //创建实例,确保全局唯一，避免重复创建客户端
    private static final OkHttpClient client = new OkHttpClient().newBuilder()
            .connectTimeout(10, TimeUnit.SECONDS)
            .readTimeout(10, TimeUnit.SECONDS)
            .writeTimeout(10, TimeUnit.SECONDS)
            .build();

    //私有静态变量：持有唯一的HttpUtil实例
    private static HttpUtil httpUtil = null;
    //无参构造私有化，禁止外部new创建实例
    private HttpUtil(){}

    //synchronized:用于确保在多线程环境中对共享资源的安全访问，避免数据不一致性
    public static synchronized HttpUtil getInstance(){
        if(httpUtil == null){
            httpUtil = new HttpUtil();
        }
        return httpUtil;
    }
    public OkHttpClient getClient(){
        return client;
    }

    public String doGet(String url, Map<String, String> headers, Map<String, String> params){
        // 1. 前置校验：URL不能为空，避免无效请求
        if (url == null || url.trim().isEmpty()) {
            return "错误：请求URL不能为空";
        }

        // 2. 拼接URL参数（核心：把Map参数转为?key1=value1&key2=value2）
        String finalUrl = url;
        if (params != null && !params.isEmpty()) {
            // 用StringBuilder拼接参数，避免字符串拼接的性能损耗
            StringBuilder paramBuilder = new StringBuilder("?");
            for (Map.Entry<String, String> entry : params.entrySet()) {
                // 跳过空的键/值，避免无效参数
                if (entry.getKey() == null || entry.getValue() == null) {
                    continue;
                }
                paramBuilder.append(entry.getKey())
                        .append("=")
                        // 手动编码特殊字符（比如中文、空格），避免URL格式错误
                        .append(URLEncoder.encode(entry.getValue(), StandardCharsets.UTF_8))
                        .append("&");
            }
            // 移除最后一个多余的&
            if (paramBuilder.length() > 1) {
                paramBuilder.deleteCharAt(paramBuilder.length() - 1);
            }
            finalUrl += paramBuilder.toString();
        }

        // 3. 构建Request对象（核心：遍历添加请求头）
        Request.Builder requestBuilder = new Request.Builder().url(finalUrl);
        // 遍历headers Map，添加所有请求头
        if (headers != null && !headers.isEmpty()) {
            for (Map.Entry<String, String> entry : headers.entrySet()) {
                // 跳过空的请求头，避免无效头
                if (entry.getKey() == null || entry.getValue() == null) {
                    continue;
                }
                requestBuilder.addHeader(entry.getKey(), entry.getValue());
            }
        }
        // 构建最终的Request（GET请求默认是GET，无需手动指定method）
        Request request = requestBuilder.build();

        // 4. 执行请求+处理响应+资源释放（核心：try-finally保证资源关闭）
        Response response = null;
        try {
            // 执行同步GET请求
            response = client.newCall(request).execute();

            // 校验响应是否成功（HTTP状态码200-299为成功）
            if (!response.isSuccessful()) {
                return "错误：请求失败，HTTP状态码：" + response.code();
            }

            // 读取响应体（string()方法会自动关闭流，但仍用finally兜底）
            ResponseBody responseBody = response.body();
            return responseBody != null ? responseBody.string() : "响应体为空";

        } catch (UnsupportedEncodingException e) {
            // 捕获编码异常（比如URLEncoder编码失败）
            return "错误：参数编码失败，原因：" + e.getMessage();
        } catch (IOException e) {
            // 捕获网络异常（比如超时、连接失败）
            return "错误：网络请求失败，原因：" + e.getMessage();
        } catch (Exception e) {
            // 捕获其他未知异常
            return "错误：请求异常，原因：" + e.getMessage();
        } finally {
            // 关键：无论请求成功/失败，都关闭Response，释放连接池资源
            if (response != null) {
                response.close();
            }
        }
    }

    /**
     * POST 请求方法（支持 JSON/表单两种请求体）
     * @param url 请求地址
     * @param headers 请求头（可选）
     * @param requestBodyType 请求体类型："json" 或 "form"
     * @param bodyParams 请求体参数（JSON传Map，表单也传Map）
     * @return 响应体字符串/错误信息
     */
    public String doPost(String url, Map<String, String> headers, String requestBodyType, Map<String, Object> bodyParams) {
        // 1. 前置校验
        if (url == null || url.trim().isEmpty()) {
            return "错误：请求URL不能为空";
        }
        if (requestBodyType == null || !("json".equals(requestBodyType) || "form".equals(requestBodyType))) {
            return "错误：请求体类型只能是json或form";
        }
        if (bodyParams == null || bodyParams.isEmpty()) {
            return "错误：请求体参数不能为空";
        }

        // 2. 构建请求体（核心：区分JSON/表单）
        RequestBody requestBody = null;
        try {
            if ("json".equals(requestBodyType)) {
                // 2.1 JSON格式：Map转JSON字符串
                String jsonBody = mapToJson(bodyParams);
                MediaType jsonMediaType = MediaType.parse("application/json; charset=utf-8");
                requestBody = RequestBody.create(jsonBody, jsonMediaType);
            } else if ("form".equals(requestBodyType)) {
                // 2.2 表单格式：Map转键值对字符串（key1=value1&key2=value2）
                StringBuilder formBuilder = new StringBuilder();
                for (Map.Entry<String, Object> entry : bodyParams.entrySet()) {
                    if (entry.getKey() == null || entry.getValue() == null) {
                        continue;
                    }
                    formBuilder.append(entry.getKey())
                            .append("=")
                            .append(URLEncoder.encode(entry.getValue().toString(), StandardCharsets.UTF_8))
                            .append("&");
                }
                if (formBuilder.length() > 1) {
                    formBuilder.deleteCharAt(formBuilder.length() - 1);
                }
                MediaType formMediaType = MediaType.parse("application/x-www-form-urlencoded; charset=utf-8");
                requestBody = RequestBody.create(formBuilder.toString(), formMediaType);
            }
        } catch (Exception e) {
            return "错误：构建请求体失败，原因：" + e.getMessage();
        }

        // 3. 构建Request对象（添加请求头+请求体）
        Request.Builder requestBuilder = new Request.Builder()
                .url(url)
                .post(requestBody); // POST请求核心：设置请求体

        // 添加请求头（自动补充Content-Type，避免调用方漏传）
        if (headers != null && !headers.isEmpty()) {
            for (Map.Entry<String, String> entry : headers.entrySet()) {
                if (entry.getKey() == null || entry.getValue() == null) {
                    continue;
                }
                requestBuilder.addHeader(entry.getKey(), entry.getValue());
            }
        }

        // 4. 执行请求+处理响应（和GET逻辑一致，重点：资源释放）
        Response response = null;
        try {
            response = client.newCall(requestBuilder.build()).execute();

            if (!response.isSuccessful()) {
                return "错误：POST请求失败，HTTP状态码：" + response.code();
            }

            ResponseBody responseBody = response.body();
            return responseBody != null ? responseBody.string() : "响应体为空";

        } catch (IOException e) {
            return "错误：网络请求失败，原因：" + e.getMessage();
        } catch (Exception e) {
            return "错误：请求异常，原因：" + e.getMessage();
        } finally {
            if (response != null) {
                response.close();
            }
        }
    }

    /**
     * 辅助方法：Map转JSON字符串（需引入JSON工具类，比如Fastjson/Jackson）
     */
    private String mapToJson(Map<String, Object> bodyParams) {
        // 示例：使用Fastjson转JSON
        return com.alibaba.fastjson2.JSON.toJSONString(bodyParams);
        // 若用Jackson：return new ObjectMapper().writeValueAsString(bodyParams);
    }

    /**
     * 文件上传专用 POST 方法（multipart/form-data）
     * @param url 请求地址（可包含URL参数，比如?conf=0.5&iou=0.7）
     * @param headers 请求头（可选，比如Token、Content-Type）
     * @param fileParamName AI接口要求的文件参数名（比如你的场景是"file"）
     * @param file 要上传的文件（MultipartFile 转 File）
     * @return 响应体字符串/错误信息
     */
    public String doPostUpload(String url, Map<String, String> headers, String fileParamName, MultipartFile file) {
        // 1. 前置校验
        if (url == null || url.trim().isEmpty()) {
            return "错误：请求URL不能为空";
        }
        if (file == null || file.isEmpty()) {
            return "错误：上传文件不能为空";
        }
        if (fileParamName == null || fileParamName.trim().isEmpty()) {
            fileParamName = "file"; // 默认文件参数名
        }

        // 2. 构建临时文件（避免文件流占用）
        File tempFile = null;
        try {
            // 创建临时文件，后缀保留原文件名
            tempFile = File.createTempFile("okhttp-upload-", file.getOriginalFilename());
            file.transferTo(tempFile); // 把MultipartFile写入临时文件
        } catch (IOException e) {
            return "错误：创建临时文件失败，原因：" + e.getMessage();
        }

        // 3. 构建multipart/form-data请求体
        MediaType mediaType = MediaType.parse(
                file.getContentType() != null ? file.getContentType() : "application/octet-stream"
        );
        MultipartBody.Builder multipartBuilder = new MultipartBody.Builder()
                .setType(MultipartBody.FORM); // 必须设置为FORM类型

        // 添加文件参数
        multipartBuilder.addFormDataPart(
                fileParamName,
                file.getOriginalFilename(),
                RequestBody.create(tempFile, mediaType)
        );

        RequestBody requestBody = multipartBuilder.build();

        // 4. 构建Request对象
        Request.Builder requestBuilder = new Request.Builder()
                .url(url)
                .post(requestBody);

        // 添加请求头
        if (headers != null && !headers.isEmpty()) {
            for (Map.Entry<String, String> entry : headers.entrySet()) {
                if (entry.getKey() == null || entry.getValue() == null) {
                    continue;
                }
                requestBuilder.addHeader(entry.getKey(), entry.getValue());
            }
        }

        // 5. 执行请求+处理响应+资源释放
        Response response = null;
        try {
            response = client.newCall(requestBuilder.build()).execute();

            if (!response.isSuccessful()) {
                return "错误：文件上传请求失败，HTTP状态码：" + response.code();
            }

            ResponseBody responseBody = response.body();
            return responseBody != null ? responseBody.string() : "响应体为空";

        } catch (IOException e) {
            return "错误：网络请求失败，原因：" + e.getMessage();
        } catch (Exception e) {
            return "错误：文件上传异常，原因：" + e.getMessage();
        } finally {
            // 关键：关闭响应+删除临时文件
            if (response != null) {
                response.close();
            }
            // 临时文件用完即删，避免磁盘占用
            if (tempFile != null && tempFile.exists()) {
                tempFile.delete();
            }
        }
    }


}
