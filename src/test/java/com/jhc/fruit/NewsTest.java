// package com.jhc.fruit;

// import com.jhc.fruit.entity.NewsImg;
// import com.jhc.fruit.common.NewsUtil;
// import com.jhc.fruit.entity.News;
// import org.jsoup.Jsoup;
// import org.jsoup.nodes.Document;
// import org.jsoup.nodes.Element;
// import org.jsoup.select.Elements;
// import org.junit.jupiter.api.Test;
// import org.springframework.boot.test.context.SpringBootTest;

// import java.net.URL;
// import java.util.ArrayList;
// import java.util.HashMap;
// import java.util.List;
// import java.util.Map;

// /**
//  * @author ：lcy
//  * @description：TODO
//  * @date ：2022/5/26 0026 10:47
//  */
// @SpringBootTest
// public class NewsTest {

//     @Test
//     public void test() throws Exception{
//         //1.定义变量
//         ArrayList<News> newList = new ArrayList<>();
//         String fromUrl = "http://www.nyxw.org.cn/xiangxi.asp?xid=24303&leibie=%CA%B5%D3%C3%BC%BC%CA%F5";

//         //获取对应url的Document对象
//         Document document = Jsoup.parse(new URL(fromUrl), 30000);

//         //获取的一篇文章内容总和
//         String subContent = "";

//         //2.根据标签中class属性获得Element对象
//         //获取class = index-module_textWrap_3yg0c的div标签中的p标签内容  ---> 文章文字内容
//         Elements contents = document.getElementsByClass("index-module_textWrap_3ygOc ");
//         //获取class = index-module_mediaWrap_213jB的div标签中的img标签内容  ---> 文章图片内容
//         Elements imgs = document.getElementsByClass("index-module_mediaWrap_213jB ");

//         //3.获取标签中的内容
//         for (Element e : contents) {
//             String content = e.getElementsByTag("p").text();
//             subContent = subContent + content;
//         }
//         for(Element e : imgs){
//             String url = e.getElementsByTag("static/img").attr("src");
//             System.out.println(url);
//             System.out.println("==================================");
//         }

//     }

//     @Test
//     public void testMethod() throws Exception{
//         //1.定义变量
//         News xinwen = null;
//         NewsImg tupian = null;
//         Map<String,List> map = new HashMap<>();
//         ArrayList<News> newList = new ArrayList<>();
//         ArrayList<List<NewsImg>> newsImgList = new ArrayList<>();
//         ArrayList<NewsImg> newImgList;
//         //2.遍历该类53页农业资讯

//         //2.1获取url
//         String fromUrl = "http://www.nyxw.org.cn/fenlei.asp?pageIndex=1&leibie=%CA%B5%D3%C3%BC%BC%CA%F5";
//         //2.2获取对应url的Document对象
//         Document document = Jsoup.parse(new URL(fromUrl), 30000);
//         //2.3获取当中存放新闻标题列表的父对象
//         Elements li = document.getElementsByTag("li");
//         //2.4获取当中各个新闻的标签
//         for(Element e : li){
//             String a = e.getElementsByTag("a").text();
//             System.out.println(a);
//             System.out.println("=========================");
//         }
//     }

//     @Test
//     public void test3() throws Exception{
//         //1.定义变量
//         News xinwen = null;
//         ArrayList<News> newList = new ArrayList<>();

//         //2.遍历该类53页农业资讯
//         for(int i = 1;i <= 53;i++){
//             //2.1获取url
//             String fromUrl = "http://www.nyxw.org.cn/fenlei.asp?pageIndex="+ String.valueOf(i) + "&leibie=%CA%B5%D3%C3%BC%BC%CA%F5";
//             //2.2获取对应url的Document对象
//             Document document = Jsoup.parse(new URL(fromUrl), 30000);
//             //2.3获取当中存放新闻标题列表的父对象
//             Elements li = document.getElementsByTag("li");
//             //2.4遍历父对象中各个子对象
//             for (int j = 11; j < 12;j++) {
//                 Element e = li.get(j);
//                 //对象初始化
//                 xinwen = new News();
//                 //获取新闻标题
//                 String title = null;
//                 title = e.getElementsByTag("a").text();
//                 xinwen.setTitle(title);
//                 //获取新闻标题对应的url地址
//                 String url = null;
//                 url = "http://www.nyxw.org.cn/" + e.getElementsByTag("a").attr("href");
//                 xinwen.setUrl(url);
//                 xinwen.setContent(NewsUtil.getContentByTitle(url));
//                 System.out.println(xinwen.getContent());
//                 newList.add(xinwen);
//             }
//         }
//     }


// }
