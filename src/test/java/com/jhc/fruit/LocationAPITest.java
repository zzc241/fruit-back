package com.jhc.fruit;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONException;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import javax.servlet.http.HttpServletRequest;
import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.Charset;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @author ：lcy
 * @description：测试根据Ip获取地理位置
 * @date ：2022/5/19 0019 11:29
 */
@SpringBootTest
public class LocationAPITest {
    /**
     *@Function
     *@Description 得到本机外网ip
     **/
    public static String getV4IP() {
        String ip = "";
        String chinaz = "http://ip.chinaz.com/";

        String inputLine = "";
        String read = "";
        try {
            URL url = new URL(chinaz);
            HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
            BufferedReader in = new BufferedReader(new InputStreamReader(urlConnection.getInputStream()));
            while ((read = in.readLine()) != null) {
                inputLine += read;
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
        //正则匹配标签，注意\\
        Pattern p = Pattern.compile("\\<dd class\\=\"fz24\">(.*?)\\<\\/dd>");
        Matcher m = p.matcher(inputLine);
        if(m.find()){
            ip = m.group(1);
        }
        return ip;
    }


    /**
     * 读取方法
     *
     * @param rd
     * @return
     * @throws IOException
     */
    private static String readAll(Reader rd) throws IOException {
        StringBuilder sb = new StringBuilder();
        int cp;
        while ((cp = rd.read()) != -1) {
            sb.append((char) cp);
        }
        return sb.toString();
    }

    /**
     * 创建链接
     *
     * @param url
     * @return
     * @throws IOException
     * @throws JSONException
     */
    private static JSONArray readJsonFromUrl(String url) throws IOException, JSONException {
        InputStream is = new URL(url).openStream();
        try {
            BufferedReader rd = new BufferedReader(new InputStreamReader(is, Charset.forName("UTF-8")));
            String jsonText = readAll(rd);
            JSONArray json = JSONArray.parseArray(jsonText);
            return json;
        } finally {
            is.close();
        }
    }
    //每天只能获取5次地理位置
    @Test
    public void test() throws Exception {
        String ip = getV4IP();
        String url = "http://freeapi.ipip.net/"+ ip;
        System.out.println(ip);
        //JSONArray json = readJsonFromUrl(url);

        //String a2 = (String)json.get(1);
        //String a3 = (String)json.get(2);

        //System.out.println(a2);
        //System.out.println(a3);
    }

}
