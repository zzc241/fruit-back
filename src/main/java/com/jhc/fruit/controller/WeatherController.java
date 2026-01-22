package com.jhc.fruit.controller;

import com.alibaba.fastjson.JSONArray;
import com.jhc.fruit.common.IpUtil;
import com.jhc.fruit.common.R;
import com.jhc.fruit.common.WeatherUtil;
import com.jhc.fruit.entity.WeatherInfo;
import com.jhc.fruit.service.impl.WeatherServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;

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

    //存储当日天气
    @PostMapping
    public R<String> saveWeather() throws Exception{
        for(int i = 1;i<=6;i++){
            if(weatherServiceImpl.getById(i) != null){
                weatherServiceImpl.removeById(i);
            }
        }
        //获取当前地理位置
        //1.获取当前请求访问的请求Ip
        String ip = IpUtil.getV4IP();
        System.out.println(ip);
        //2.获取地理位置
        JSONArray json = IpUtil.readJsonFromUrl("http://freeapi.ipip.net/"+ip);
        String province = (String)json.get(2);
        //获取json格式的天气数据
        String weatherData = WeatherUtil.getWeatherData(province);
        //获取昨天天气数据
        WeatherInfo yesterday = WeatherUtil.GetYesterdayWeather(weatherData);
        yesterday.setId(1);
        weatherServiceImpl.save(yesterday);
        for(int i = 0;i <= 4; i ++){
            WeatherInfo info = WeatherUtil.GetWeather(i,weatherData);
            info.setId(i + 2);
            weatherServiceImpl.save(info);
        }
        return R.success("获取天气成功");
    }

    //对获取到的天气数据进行处理
    @GetMapping("/{id}")
    public R<WeatherInfo> getWeather(@PathVariable int id){
        WeatherInfo weatherInfo = weatherServiceImpl.getById(id);
        return R.success(weatherInfo);
    }
}
