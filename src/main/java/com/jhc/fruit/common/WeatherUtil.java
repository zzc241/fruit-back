package com.jhc.fruit.common;



import com.alibaba.fastjson2.JSONArray;
import com.alibaba.fastjson2.JSONObject;
import com.jhc.fruit.entity.WeatherInfo;

import org.apache.commons.io.IOUtils;
import org.w3c.dom.Document;

import java.io.*;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLEncoder;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.zip.GZIPInputStream;

/**
 * @author ：lcy
 * @description：实时获取天气预报工具类
 * @date ：2022/5/17 0017 15:48
 */
public class WeatherUtil {
    //返回json数据
    public static void getWeather(String cityName){
        try{
            //参数化url，并设置编码
            String city = URLEncoder.encode(cityName,"utf-8");
            //拼接地址
            String apiUrl = String.format("http://wthrcdn.etouch.cn/weather_mini?city=%s", city);
            //开始请求
            URL url = new URL(apiUrl);
            URLConnection open = url.openConnection();
            InputStream input = open.getInputStream();
            // 这里转换为String
            String result = IOUtils.toString(input, "utf-8");
            // 输出
            System.out.println(result);
            System.out.println("你好");
        }catch (Exception e){
            e.printStackTrace();
        }
    }
    public static String getWeatherData(String cityname) {

        StringBuilder sb = new StringBuilder();
        //字符输入流中读取文本
        BufferedReader reader = null;

        try {
            //初始化URL对象
            URL url = new URL("http://wthrcdn.etouch.cn/weather_mini?city=" + cityname);
            //获得URL对象的连接
            URLConnection conn = url.openConnection();
// 开始时间
            long stime = System.currentTimeMillis();
            //通过输入流获取URL中的内容
            InputStream is = conn.getInputStream();
            // 结束时间
            long etime = System.currentTimeMillis();
            // 计算执行时间
            System.out.printf("执行时长：%d 毫秒.", (etime - stime));

            //将流中的内容进行解压缩
            GZIPInputStream gzin = new GZIPInputStream(is);

            // 设置读取流的编码格式，自定义编码
            InputStreamReader isr = new InputStreamReader(gzin, "utf-8");

            reader = new BufferedReader(isr);

            String line = null;
            while((line = reader.readLine()) != null){
                sb.append(line + " ");

            }

            reader.close();
        } catch (Exception e) {
            e.printStackTrace();
        }

        return sb.toString();

    }

    /**
     * 获取昨日天气数据
     * @param weatherInfoByJson
     * @return
     */
    public static WeatherInfo GetYesterdayWeather(String weatherInfoByJson){
        //json天气数据
        JSONObject dataOfJson = JSONObject.parseObject(weatherInfoByJson);

        //判断获取状态是否成功，失败则返回null
        if (dataOfJson.getIntValue("status") != 1000){
            return null;
        }

        // 创建WeatherInfo对象，提取所需的天气信息
        WeatherInfo weatherInfo = new WeatherInfo();

        // 获取当前日期：日期、星期
        Calendar cal = Calendar.getInstance(); // Calendar类的实例化
        SimpleDateFormat sdf1 = new SimpleDateFormat("yyyy年MM月dd日"); // 时间的格式化
        //设置日
        int day = cal.get(Calendar.DATE);
        cal.set(Calendar.DATE, day - 1);

        weatherInfo.setDate(sdf1.format(cal.getTime())); // 时间

        // 从json数据中提取数据：城市、温度、小提醒
        dataOfJson = JSONObject.parseObject(dataOfJson.getString("data"));
        // 获取昨天的天气预报信息：最高温度、最低温度、天气
        JSONObject yesterday = dataOfJson.getJSONObject("yesterday");
        weatherInfo.setType(yesterday.getString("type")); // 天气
        return weatherInfo;
    }

    /**
     * 获取当日天气数据
     * @param weatherInfoByJson JSON格式的天气数据
     * @return
     */
    public static WeatherInfo GetWeather(String weatherInfoByJson){

        JSONObject dataOfJson = JSONObject.parseObject(weatherInfoByJson); // json天气数据
        //判断获取状态是否成功，失败则返回null
        if(dataOfJson.getIntValue("status") != 1000){
            return null;
        }

        // 创建WeatherInfo对象，提取所需的天气信息
        WeatherInfo weatherInfo = new WeatherInfo();

        // 获取当前日期：日期、星期
        // Calendar类的实例化
        Calendar cal = Calendar.getInstance();

        // 时间的格式化
        SimpleDateFormat sdf1 = new SimpleDateFormat("yyyy年MM月dd日");
        weatherInfo.setDate(sdf1.format(cal.getTime())); // 时间

        // 从json数据中提取数据：城市、温度、小提醒
        dataOfJson = JSONObject.parseObject(dataOfJson.getString("data"));

        // 获取今天的天气预报信息：最高温度、最低温度、天气
        JSONArray forecast = dataOfJson.getJSONArray("forecast");
        JSONObject result = forecast.getJSONObject(0);
        // 天气
        weatherInfo.setType(result.getString("type"));
        return weatherInfo;

    }

    /**
     * 获取某日天气数据
     * @param day   某日：0代表当日，1代表明天
     * @param weatherInfoByJson 传入的JSON格式天气数据
     * @return
     */
    public static WeatherInfo GetWeather(int day,String weatherInfoByJson){

        JSONObject dataOfJson = JSONObject.parseObject(weatherInfoByJson); // json天气数据
        //判断获取状态是否成功，失败则返回null
        if(dataOfJson.getIntValue("status") != 1000){
            return null;
        }

        // 创建WeatherInfo对象，提取所需的天气信息
        WeatherInfo weatherInfo = new WeatherInfo();

        // 获取当前日期：日期、星期
        // Calendar类的实例化
        Calendar cal = Calendar.getInstance();

        //设置日
        int days = cal.get(Calendar.DATE);
        cal.set(Calendar.DATE, days + day);

        // 时间的格式化
        SimpleDateFormat sdf1 = new SimpleDateFormat("yyyy年MM月dd日");
        weatherInfo.setDate(sdf1.format(cal.getTime())); // 时间
        SimpleDateFormat sdf2 = new SimpleDateFormat("EEEE");

        // 从json数据中提取数据：城市、温度、小提醒
        dataOfJson = JSONObject.parseObject(dataOfJson.getString("data"));

        // 获取今天的天气预报信息：最高温度、最低温度、天气
        JSONArray forecast = dataOfJson.getJSONArray("forecast");
        JSONObject result = forecast.getJSONObject(day);
        // 天气
        weatherInfo.setType(result.getString("type"));
        return weatherInfo;

    }
}

