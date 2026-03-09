package com.jhc.fruit.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.jhc.fruit.entity.WeatherInfo;


public interface WeatherService extends IService<WeatherInfo> {
    public WeatherInfo getByOrchardId(Long orchardId);

    public WeatherInfo saveByHttp(Long orchardId) throws Exception;
}
