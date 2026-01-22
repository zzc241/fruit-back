package com.jhc.fruit;


import com.alibaba.fastjson.JSONObject;
import com.jhc.fruit.entity.IdentifyInfo;
import org.apache.http.HttpResponse;
import org.apache.http.StatusLine;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.HttpClients;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;


import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;

/**
 * @author ：挽風
 * @description：TODO
 * @date ：2022/8/4 0004 14:56
 */
@SpringBootTest
public class ProgressTest {
    /**
     * 根据url获取图片方法优化 ：用时1520ms
     * @throws Exception
     */
    @Test
    public void test() throws Exception {
        String url = "http://36.134.4.166:5000/tmp/draw/fruit.jpg";
        //获取方法开始的时间
        long stime = System.currentTimeMillis();

        BufferedImage read = ImageIO.read(new URL(url));

        //获取方法结束时的时间
        long etime = System.currentTimeMillis();
        System.out.printf("第一次连接时长：%d 毫秒.", (etime - stime));
    }

    /**
     * 上传图片old 650
     * @throws Exception
     */
    @Test
    public void test3() throws Exception{
        // 开始时间
        long stime = System.currentTimeMillis();

        IdentifyInfo info = new IdentifyInfo();
        //拼接访问的python接口地址
        String url = "http://36.134.4.166:5000/url?url=";
        String img = "https://fruitphtoto.lookyousmileface.icu/08f2b79a-84fc-4d9e-84f8-29eb4719a7ab5.jpg";
        //获取连接对象并设置连接属性
        URL testUrl = new URL(url + img);
        URLConnection conn = testUrl.openConnection();
        conn.setRequestProperty("accept", "*/*");
        conn.setRequestProperty("connection", "Keep-Alive");
        conn.setRequestProperty("user-agent",
                "Mozilla/4.0 (compatible; MSIE 6.0; Windows NT 5.1;SV1)");
        //连接
        conn.connect();


        //获取BufferedRead对象读取流中的数据
        BufferedReader in = new BufferedReader(new InputStreamReader(conn.getInputStream()));
        String line,result = "";
        while((line = in.readLine()) != null){
            result += line;
        }
        System.out.println(result);
        // 结束时间
        long etime = System.currentTimeMillis();
        // 计算执行时间
        System.out.printf("第一次连接时长：%d 毫秒.", (etime - stime));
    }

    /**
     * 上传图片进行识别功能 504ms
     * @throws Exception
     */
    @Test
    public void test2() throws Exception{
        //获取方法开始的时间
        long stime = System.currentTimeMillis();

        URL url = new URL("http://36.134.4.166:5000/url?url=https://fruitphtoto.lookyousmileface.icu/08f2b79a-84fc-4d9e-84f8-29eb4719a7ab5.jpg");
        InputStream is = url.openStream();
        BufferedReader br = new BufferedReader(new InputStreamReader(is));
        String line = null;
        String result = "";
        while((line = br.readLine()) != null) {
            System.out.println(line);
            result += line;
        }
        System.out.println(result);
        //获取方法结束时的时间
        long etime = System.currentTimeMillis();
        System.out.printf("第一次连接时长：%d 毫秒.", (etime - stime));
    }

    @Test
    public void test4() throws Exception{
        //获取方法开始的时间
        long stime = System.currentTimeMillis();

        //获取上传方法返回的识别数据
        URL finalResult = new URL("http://36.134.4.166:5000/upload");


        URLConnection connection = finalResult.openConnection();



        BufferedReader ris = new BufferedReader(new InputStreamReader(connection.getInputStream()));


        String rLine,fResult = "";

        while((rLine = ris.readLine()) != null){
            fResult += rLine;
        }


        System.out.println("fResult:"+ fResult);
        JSONObject dataOfJson = JSONObject.parseObject(fResult);
        String draw_url = dataOfJson.getString("all_draw_url");
        String numInformation = dataOfJson.getString("all_information");
        String ripInformation = dataOfJson.getString("ripe_information");

        System.out.println("draw_url:" + draw_url);
        System.out.println("numInformation:" + numInformation);
        System.out.println("ripInformation:" + ripInformation);


        //获取方法结束时的时间
        long etime = System.currentTimeMillis();
        System.out.printf("第一次连接时长：%d 毫秒.", (etime - stime));
    }

    @Test
    public void test5(){
        System.out.println(3*100/4);
    }
}
