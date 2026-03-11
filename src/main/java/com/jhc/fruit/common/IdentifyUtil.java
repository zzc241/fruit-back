package com.jhc.fruit.common;

import com.alibaba.fastjson2.JSONObject;
import com.jhc.fruit.entity.IdentifyInfo;
// import org.bytedeco.javacv.FrameGrabber;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;

/**
 * @author ：lcy
 * @description：TODO
 * @date ：2022/6/7 0007 18:24
 */
public class IdentifyUtil {

        public static IdentifyInfo identify(String imgName) throws Exception {
            //改进前获取：获取时间稍长 大约多了100ms左右；并且代码冗长

//            //拼接访问的python接口地址
//            String url = "http://36.134.4.166:5000/url?url=";//http://re0gyrgsz.hn-bkt.clouddn.com/
//            String img = imgName;  //"8847dce8-c7ff-42dc-90b2-a7404a457e926.jpg"
//            //获取连接对象并设置连接属性
//            URL testUrl = new URL(url + img);
//            URLConnection conn = testUrl.openConnection();
//            conn.setRequestProperty("accept", "*/*");
//            conn.setRequestProperty("connection", "Keep-Alive");
//            conn.setRequestProperty("user-agent",
//                    "Mozilla/4.0 (compatible; MSIE 6.0; Windows NT 5.1;SV1)");
//
//            // 开始时间
//            long stime = System.currentTimeMillis();
//            //连接
//            conn.connect();
//            // 结束时间
//            long etime = System.currentTimeMillis();
//            // 计算执行时间
//            System.out.printf("第一次连接时长：%d 毫秒.", (etime - stime));
//
//            //获取BufferedRead对象读取流中的数据
//            BufferedReader in = new BufferedReader(new InputStreamReader(conn.getInputStream()));
//            String line,result = "";
//            while((line = in.readLine()) != null){
//                result += line;
//            }
            //1.创建识别信息对象
            IdentifyInfo info = new IdentifyInfo();
            //2.上传图片进行识别
            String identifyUrl = "http://36.134.4.166:5000/url?url=" + imgName;
            URL url = new URL(identifyUrl);
            InputStream is = url.openStream();
            BufferedReader br = new BufferedReader(new InputStreamReader(is));
            String line = null;
            String result = "";
            while((line = br.readLine()) != null) {
                System.out.println(line);
                result += line;
            }

            //获取上传方法返回的识别数据
            URL finalResult = new URL(result);


            URLConnection connection = finalResult.openConnection();



            BufferedReader ris = new BufferedReader(new InputStreamReader(connection.getInputStream()));


            String rLine,fResult = "";

            while((rLine = ris.readLine()) != null){
                fResult += rLine;
            }


            System.out.println("fResult:"+ fResult);
            JSONObject dataOfJson = JSONObject.parseObject(fResult);
            String draw_count = dataOfJson.getString("all_draw_url");
            String draw_ripe = dataOfJson.getString("ripe_draw_url");
            String numInformation = dataOfJson.getString("all_information");
            String ripInformation = dataOfJson.getString("ripe_information");
            if(numInformation == null || numInformation == ""){
                return null;
            }
            String[] split = numInformation.split(",");

            info.setImgCount(draw_count);
            info.setImgRipe(draw_ripe);
            switch(split[0]){
                case "orange":
                    split[0] = "橙子";
                    break;
                case "chestnut":
                    split[0] = "板栗";
                    break;
                case "strawberry":
                    split[0] = "草莓";
                    break;
                case "tomato":
                    split[0] = "番茄";
                    break;
            }
            info.setFruitName(split[0]);
            Integer countNum = Integer.parseInt(split[1]);
            Integer ripeNum = Integer.parseInt(ripInformation.split(",")[1]);
            info.setFruitNum(countNum);
            info.setRipeNum(Long.valueOf(ripeNum));
            info.setTreeRipePercent((long) (ripeNum*100/countNum));
            return info;
        }
}
