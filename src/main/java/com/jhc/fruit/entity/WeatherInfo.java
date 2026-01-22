package com.jhc.fruit.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

/**
 * @author ：lcy
 * @description：TODO
 * @date ：2022/5/17 0017 16:27
 */
@Data
@TableName("weather")
public class WeatherInfo {
    private int id;

    private String date; //时间

    private String week; //星期

    private String location; //城市名

    private String type; //天气类型

    @TableField("high_temp")
    private String highTemp; //最高温度
    @TableField("low_temp")
    private String lowTemp; //最低温度

    private String fx; //风向

    private String wind;//风力
}




