package com.jhc.fruit;

import com.jhc.fruit.common.WeatherUtil;
import com.jhc.fruit.entity.WeatherInfo;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.Arrays;

/**
 * @author ：lcy
 * @description：TODO
 * @date ：2022/5/17 0017 15:58
 */
@SpringBootTest
public class WeatherTest {
    @Test
    public void test() throws Exception {
        String weatherData = WeatherUtil.getWeatherData("南充");
        System.out.println(weatherData);
        WeatherInfo weatherInfo = WeatherUtil.GetYesterdayWeather(weatherData);
        System.out.println(weatherInfo);
    }
    @Test
    public void test1() throws Exception {
        String weatherData = WeatherUtil.getWeatherData("南充");
        System.out.println(weatherData);
        WeatherInfo weatherInfo = WeatherUtil.GetWeather(weatherData);
        System.out.println(weatherInfo);
    }

    @Test
    public void test2() throws Exception {
        String weatherData = WeatherUtil.getWeatherData("金华");
        System.out.println(weatherData);
        WeatherInfo weatherInfo = WeatherUtil.GetWeather(1,weatherData);
        System.out.println(weatherInfo);
    }
}
