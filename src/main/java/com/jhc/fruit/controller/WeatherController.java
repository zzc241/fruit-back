package com.jhc.fruit.controller;

import com.jhc.fruit.common.R;
import com.jhc.fruit.entity.WeatherInfo;
import com.jhc.fruit.service.WeatherService;

import io.swagger.annotations.ApiOperation;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/**
 * @author ：lcy
 * @description：TODO
 * @date ：2022/5/19 0019 9:23
 */
@RestController
@RequestMapping("/weather")
public class WeatherController {
    @Autowired
    WeatherService weatherService;
    //存储当日天气
    @GetMapping("{orchardId}")
    @ApiOperation("获取天气信息")
    public R<WeatherInfo> saveWeather(@PathVariable Long orchardId) throws Exception{

        WeatherInfo weatherInfo = weatherService.getByOrchardId(orchardId);
        LocalDateTime now = LocalDateTime.now();
        //如果数据库中已有当天的天气信息，则直接返回
        if(weatherInfo != null && weatherInfo.getDate().equals(now.format(DateTimeFormatter.ofPattern("yyyy-MM-dd")))){
            return R.success(weatherInfo);
        }
        weatherInfo = weatherService.saveByHttp(orchardId);
        
        return R.success(weatherInfo);
        
    }

    
}
