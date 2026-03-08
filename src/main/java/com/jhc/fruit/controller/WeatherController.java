package com.jhc.fruit.controller;

import com.alibaba.fastjson.JSONArray;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.jhc.fruit.common.BaseContext;
import com.jhc.fruit.common.IpUtil;
import com.jhc.fruit.common.R;
import com.jhc.fruit.common.WeatherUtil;
import com.jhc.fruit.entity.WeatherInfo;
import com.jhc.fruit.service.impl.EmployeeServiceImpl;
import com.jhc.fruit.service.impl.WeatherServiceImpl;

import io.swagger.annotations.ApiOperation;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.concurrent.TimeUnit;

/**
 * @author ：lcy
 * @description：TODO
 * @date ：2022/5/19 0019 9:23
 */
@RestController
@RequestMapping("/weather")
public class WeatherController {
    @Autowired
    WeatherServiceImpl weatherServiceImpl;
    @Autowired
    private EmployeeServiceImpl employeeServiceImpl;

    //存储当日天气
    @PostMapping
    @ApiOperation("获取天气信息")
    public R<WeatherInfo> saveWeather(@RequestBody Long orchardId) throws Exception{
        LambdaQueryWrapper<WeatherInfo> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(WeatherInfo::getOrchardId, orchardId);
        queryWrapper.orderByDesc(WeatherInfo::getReportTime);
        queryWrapper.last("LIMIT 1");
        WeatherInfo existingWeather = weatherServiceImpl.getOne(queryWrapper);
        LocalDateTime now = LocalDateTime.now();
        //如果数据库中已有当天的天气信息，则直接返回
        if(existingWeather != null && existingWeather.getDate().equals(now.format(DateTimeFormatter.ofPattern("yyyy-MM-dd")))){
            return R.success(existingWeather);
        }
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
                    return R.error("天气接口调用失败，状态码：" + response.code());
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
                weatherServiceImpl.save(weatherInfo);
    
                return R.success(weatherInfo);
            }
            
        } catch (IOException e) {
            e.printStackTrace();
            return R.error("天气信息获取异常：" + e.getMessage());
        }
        
    }
    @GetMapping("/getWeather/add")
    @ApiOperation("保存天气信息")
    //传入果园id
    public R<String> getWeather() throws Exception {
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
                    return R.error("天气接口调用失败，状态码：" + response.code());
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
                //添加果园id
                Long userId = BaseContext.getId();
                Long orchardId = employeeServiceImpl.getOrchardIdByEmpId(userId);
                weatherInfo.setOrchardId(orchardId);
                weatherServiceImpl.save(weatherInfo);
            }
            
        } catch (IOException e) {
            e.printStackTrace();
            return R.error("天气信息获取异常：" + e.getMessage());
        }
        return R.success("添加天气成功");
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
    //对获取到的天气数据进行处理
    @GetMapping("/{id}")
    public R<WeatherInfo> getWeather(@PathVariable int id){
        WeatherInfo weatherInfo = weatherServiceImpl.getById(id);
        return R.success(weatherInfo);
    }
}
