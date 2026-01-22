package com.jhc.fruit;

import org.apache.http.HttpRequest;
import org.junit.jupiter.api.Test;
import org.junit.runner.Request;
import org.springframework.boot.test.context.SpringBootTest;

import javax.servlet.ServletRequest;
import javax.servlet.http.HttpServletRequest;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * @author ：lcy
 * @description：TODO
 * @date ：2022/6/2 0002 10:41
 */
@SpringBootTest
public class PythonApiTest {
    //获取外网ip
    @Test
    public void test() throws Exception{
        //获取外网地址的网站
        String ip = "http://pv.sohu.com/cityjson?ie=utf-8";
        String inputLine = "";
        String read = "";
        try{
            URL url = new URL(ip);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("POST");
            BufferedReader in = new BufferedReader(new InputStreamReader(url.openStream()));
            while((read = in.readLine()) != null){
                inputLine +=read;
            }
            System.out.println(inputLine);  //var returnCitySN = {"cip": "122.227.41.218", "cid": "330700", "cname": "浙江省金华市"};
            String subString = inputLine.substring(28,42);
            System.out.println("外网地址：" + subString);
            in.close();
        } catch (Exception e){
            e.printStackTrace();
        }
    }

    @Test
    public void testModel(){
        String ip = "http://172.16.1.151:5000/";
        String inputLine = "";
        String read = "";
        try{
            URL url = new URL(ip);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("POST");
            BufferedReader in = new BufferedReader(new InputStreamReader(url.openStream()));
            while((read = in.readLine()) != null){
                inputLine +=read;
            }
            System.out.println(inputLine);
            in.close();
        } catch (Exception e){
            e.printStackTrace();
        }
    }


}
