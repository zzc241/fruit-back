// package com.jhc.fruit;
// import com.alibaba.fastjson.JSONObject;
// import org.junit.jupiter.api.Test;

// import org.springframework.boot.test.context.SpringBootTest;

// import java.io.*;
// import java.net.URL;
// import java.net.URLConnection;


// /**
//  * @author ：lcy
//  * @description：TODO
//  * @date ：2022/5/23 0023 14:33
//  */
// @SpringBootTest
// public class ModelTest {
//     @Test
//     public void test() throws Exception{
//         String url = "http://192.168.72.209:5000/url?url=http://rcgwg4rwn.hn-bkt.clouddn.com/";
//         String img = "8847dce8-c7ff-42dc-90b2-a7404a457e926.jpg";
//         String upload = "http://192.168.72.209:5000/upload";
//         URL testUrl = new URL(url + img);
//         URLConnection conn = testUrl.openConnection();
//         conn.setRequestProperty("accept", "*/*");
//         conn.setRequestProperty("connection", "Keep-Alive");
//         conn.setRequestProperty("user-agent",
//                 "Mozilla/4.0 (compatible; MSIE 6.0; Windows NT 5.1;SV1)");
//         //连接
//         conn.connect();

//         BufferedReader in = new BufferedReader(new InputStreamReader(conn.getInputStream()));
//         String line,result = "";
//         while((line = in.readLine()) != null){
//             result += line;
//         }
//         result = result.substring(5);

//         URL finalResult = new URL(result);
//         URLConnection connection = finalResult.openConnection();
//         BufferedReader ris = new BufferedReader(new InputStreamReader(connection.getInputStream()));
//         String rLine,fResult = "";
//         while((rLine = ris.readLine()) != null){
//             fResult += rLine;
//         }
//         System.out.println(fResult);
//         JSONObject dataOfJson = JSONObject.parseObject(fResult);
//         String draw_url = dataOfJson.getString("draw_url");
//         String information = dataOfJson.getString("information");
//         System.out.println(draw_url);
//         String[] split = information.split(",");
//         System.out.println("一号位：" + split[0]);
//         System.out.println("二号位：" + split[1]);
//         System.out.println("总体身位：" + information);
//     }





// }
