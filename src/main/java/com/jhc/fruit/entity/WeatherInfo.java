package com.jhc.fruit.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Data;

/**
 * @author ：lcy
 * @description：TODO
 * @date ：2022/5/17 0017 16:27
 */
@Data
@TableName("weather")
public class WeatherInfo {
    private Long id;

    private Long orchardId;

    private String date; //时间

    private Integer week; //星期

    @JsonProperty("weather")
    private String type; //天气类型
    // ====== 新增字段（匹配API返回的数据）=======
    
    private String province; //省份
    
    private String city; //城市
    
    private String district; //区县
    
    private String adcode; //行政区划代码
    
    @JsonProperty("weather_icon")
    @TableField("weather_icon")
    private String weatherIcon; //天气图标代码
    
    private Double temperature; //当前温度
    
    @JsonProperty("wind_direction")
    @TableField("wind_direction")
    private String windDirection; //风向（如果fx不够用）
    
    @JsonProperty("wind_power")
    @TableField("wind_power")
    private String windPower; //风力（如果wind不够用）
    
    private Integer humidity; //湿度
    
    @JsonProperty("report_time")
    @TableField("report_time")
    private String reportTime; //报告时间
}