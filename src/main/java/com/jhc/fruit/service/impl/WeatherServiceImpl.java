package com.jhc.fruit.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.jhc.fruit.dao.WeatherDao;
import com.jhc.fruit.entity.WeatherInfo;
import com.jhc.fruit.service.WeatherService;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

import java.io.IOException;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.concurrent.TimeUnit;

import org.springframework.stereotype.Service;

/**
 * @author ：lcy
 * @description：TODO
 * @date ：2022/5/19 0019 10:40
 */
@Service
public class WeatherServiceImpl extends ServiceImpl<WeatherDao, WeatherInfo> implements WeatherService {

    @Override
    public WeatherInfo getByOrchardId(Long orchardId) {
        LambdaQueryWrapper<WeatherInfo> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(WeatherInfo::getOrchardId, orchardId);
        queryWrapper.orderByDesc(WeatherInfo::getReportTime);
        queryWrapper.last("LIMIT 1");
        WeatherInfo weatherInfo = this.getOne(queryWrapper);
        return weatherInfo;
    }

    @Override
    public WeatherInfo saveByHttp(Long orchardId) throws Exception {
        OkHttpClient client = new OkHttpClient.Builder()
                .connectTimeout(10, TimeUnit.SECONDS)
                .readTimeout(10, TimeUnit.SECONDS)
                .build();
        
        try {
            // 1. 构建请求
            Request request = new Request.Builder()
                    .url("https://uapis.cn/api/v1/misc/weather") // 确保这是正确的URL
                    .get()
                    .addHeader("Content-Type", "application/json")
                    // 如果需要API密钥，在这里添加
                    // .addHeader("Authorization", "Bearer YOUR_API_KEY")
                    .build();
            
            // 2. 发送同步请求
            try (Response response = client.newCall(request).execute()) {
                if (!response.isSuccessful()) {
                    log.error("天气接口调用失败，状态码：" + response.code());
                }
                
                // 3. 解析响应
                String responseBody = response.body().string();
                WeatherInfo weatherInfo = parseWeatherInfo(responseBody);
                LocalDate today = LocalDate.now();
                
                // 格式化日期为 yyyy-MM-dd
                DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
                String dateStr = today.format(dateFormatter);
                
                // 获取星期几（数字表示，1=周一，7=周日）
                DayOfWeek dayOfWeek = today.getDayOfWeek();
                int dayOfWeekNumber = dayOfWeek.getValue();
                weatherInfo.setDate(dateStr);
                weatherInfo.setWeek(dayOfWeekNumber);
                weatherInfo.setOrchardId(orchardId);
                this.save(weatherInfo);
    
                return weatherInfo;
            }
            
        } catch (IOException e) {
            e.printStackTrace();
            throw new RuntimeException("天气接口调用失败");
        }
    }
    private WeatherInfo parseWeatherInfo(String jsonResponse) {
        // 使用Jackson或Gson解析JSON
        ObjectMapper mapper = new ObjectMapper();
        try {
            // 如果返回格式直接就是WeatherInfo
            return mapper.readValue(jsonResponse, WeatherInfo.class);
            
            // 如果返回格式有嵌套，需要先获取data字段
            // JsonNode root = mapper.readTree(jsonResponse);
            // JsonNode dataNode = root.path("data");
            // return mapper.treeToValue(dataNode, WeatherInfo.class);
            
        } catch (JsonProcessingException e) {
            e.printStackTrace();
            throw new RuntimeException("天气数据解析失败");
        }
    }
}
