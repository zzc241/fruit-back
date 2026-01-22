package com.jhc.fruit;

import com.jhc.fruit.entity.News;
import org.apache.http.HttpEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;


import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;

/**
 * @author ：lcy
 * @description：TODO
 * @date ：2022/5/19 0019 16:05
 */
@SpringBootTest
public class HtmlParseTest {

    @Test
    public void clientTest() throws IOException {

        // 获取http客户端
        CloseableHttpClient client = HttpClients.createDefault();
        // 通过httpget方式来实现我们的get请求
        HttpGet httpGet = new HttpGet("http://172.16.1.151:5000/upload");
        // 开始时间
        long stime = System.currentTimeMillis();
        // 通过client调用execute方法，得到我们的执行结果就是一个response，所有的数据都封装在response里面了
        CloseableHttpResponse Response = client.execute(httpGet);
        // 结束时间
        long etime = System.currentTimeMillis();
        // 计算执行时间
        System.out.printf("执行时长：%d 毫秒.", (etime - stime));
        // HttpEntity
        // 是一个中间的桥梁，在httpClient里面，是连接我们的请求与响应的一个中间桥梁，所有的请求参数都是通过HttpEntity携带过去的
        // 所有的响应的数据，也全部都是封装在HttpEntity里面
        HttpEntity entity = Response.getEntity();
        // 通过EntityUtils 来将我们的数据转换成字符串
        String str = EntityUtils.toString(entity, "UTF-8");

        // EntityUtils.toString(entity)
        System.out.println(str);
        // 关闭
        Response.close();
    }

    @Test
    public void test() throws Exception{
        //获取请求
        String url = "https://search.jd.com/Search?keyword=java";
        //解析网页,获得的Document对象就是浏览器中的Document对象，可以执行js中对应的方法
        Document document = Jsoup.parse(new URL(url),30000);
        //获取名字对应的元素
        Element element = document.getElementById("J_goodsList");
        //获取当中所有的li元素
        Elements li = element.getElementsByTag("li");
        //获取元素中的内容
        for (Element el: li) {
            String attr = el.getElementsByTag("static/img").eq(0).attr("source-data-lazy-img");
            String price = el.getElementsByClass("p-price").eq(0).text();
            String title = el.getElementsByClass("p-name").eq(0).text();
            System.out.println("=======================================");
            System.out.println(attr);
            System.out.println(price);
            System.out.println(title);
        }
    }

    @Test
    public void test1() throws Exception{
        String fromUrl = "http://www.nyxw.org.cn/fenlei.asp?pageIndex=1&leibie=%CA%B5%D3%C3%BC%BC%CA%F5";
        Document document = Jsoup.parse(new URL(fromUrl),30000);
        Elements modbodyJ = document.getElementsByClass("modbody_j");
        for (Element e:modbodyJ
             ) {
            Elements li = e.getElementsByTag("li");
            for (Element el: li
                 ) {
                String url = "http://www.nyxw.org.cn/" + el.getElementsByTag("a").attr("href");
                String content = el.getElementsByTag("a").text();
                System.out.println(url);
                System.out.println(content);
                System.out.println("======================================================");
            }
        }
    }

    @Test
    public void test3() throws Exception{

            //1.定义变量
            ArrayList<News> newList = new ArrayList<>();
            String fromUrl = "http://www.nyxw.org.cn/fenlei.asp?pageIndex=1&leibie=%CA%B5%D3%C3%BC%BC%CA%F5";
            Document document = Jsoup.parse(new URL(fromUrl), 30000);
            Elements modbodyJ = document.getElementsByClass("modbody_j");
            for (Element e : modbodyJ
            ) {
                Elements li = e.getElementsByTag("li");
                String url = null;
                String content = null;
                News xinwen = null;
                for (Element el : li
                ) {
                    xinwen = new News();
                    url = "http://www.nyxw.org.cn/" + el.getElementsByTag("a").attr("href");
                    content = el.getElementsByTag("a").text();
                    xinwen.setUrl(url);
                    xinwen.setTitle(content);
                    newList.add(xinwen);
                }
            }
        System.out.println(newList);
        }
}
