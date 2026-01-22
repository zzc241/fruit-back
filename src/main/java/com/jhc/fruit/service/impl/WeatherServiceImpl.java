package com.jhc.fruit.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.jhc.fruit.dao.WeatherDao;
import com.jhc.fruit.entity.WeatherInfo;
import com.jhc.fruit.service.WeatherService;
import org.springframework.stereotype.Service;

/**
 * @author ：lcy
 * @description：TODO
 * @date ：2022/5/19 0019 10:40
 */
@Service
public class WeatherServiceImpl extends ServiceImpl<WeatherDao, WeatherInfo> implements WeatherService {
}
