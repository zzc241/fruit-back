package com.jhc.fruit.common;

import com.jhc.fruit.entity.News;
import com.jhc.fruit.entity.NewsImg;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.net.URL;
import java.util.ArrayList;
import java.util.List;

/**
 * @author ：lcy
 * @description：TODO
 * @date ：2022/5/19 0019 20:31
 */
public class NewsUtil {
    /**
     * 获取新闻对象列表
     * @return
     * @throws Exception
     */
    public static List<News> getNews() throws Exception {
        //1.定义变量
        News xinwen = null;
        ArrayList<News> newList = new ArrayList<>();

        //2.遍历该类53页农业资讯
        for(int i = 1;i <= 53;i++){
            //2.1获取url
            String fromUrl = "http://www.nyxw.org.cn/fenlei.asp?pageIndex="+ String.valueOf(i) + "&leibie=%CA%B5%D3%C3%BC%BC%CA%F5";
            //2.2获取对应url的Document对象
            Document document = Jsoup.parse(new URL(fromUrl), 30000);
            //2.3获取当中存放新闻标题列表的父对象
            Elements li = document.getElementsByTag("li");
            //2.4遍历父对象中各个子对象
            for (int j = 11; j < li.size();j++) {
                Element e = li.get(j);
                //对象初始化
                xinwen = new News();
                //获取新闻标题
                String title = null;
                title = e.getElementsByTag("a").text();
                xinwen.setTitle(title);
                //获取新闻标题对应的url地址
                String url = null;
                url = "http://www.nyxw.org.cn/" + e.getElementsByTag("a").attr("href");
                xinwen.setUrl(url);
                String content = getContentByTitle(url);
                xinwen.setContent(content);
                if(!content.equals("")&&!content.isEmpty()){
                    newList.add(xinwen);
                }
            }
        }
        return newList;
    }


    /**
     * 根据新闻标题获取当中的内容
     * @return
     */
    public static String getContentByTitle(String url) throws Exception{
        //1.定义变量
        ArrayList<News> newList = new ArrayList<>();

        //获取对应url的Document对象
        Document document = Jsoup.parse(new URL(url), 30000);

        //获取的一篇文章内容总和
        String subContent = "";

        //2.根据标签中class属性获得Element对象
        //获取class = index-module_textWrap_3yg0c的div标签中的p标签内容  ---> 文章文字内容
        Elements contents = document.getElementsByClass("index-module_textWrap_3ygOc ");

        //3.获取标签中的内容
        for (Element e : contents) {
            String content = e.getElementsByTag("p").text();
            subContent = subContent + content;
        }
        return subContent;
    }

    /**
     * 根据新闻标题获取文章中图片
     * @param newsId 对应新闻文章id
     * @param url 文章url
     * @return
     * @throws Exception
     */
    public static ArrayList<NewsImg> getImgByTitle(Long newsId,String url) throws Exception {
        //1.定义变量
        ArrayList<NewsImg> newsImgList = new ArrayList<>();
        NewsImg newsImg = null;

        //获取对应url的Document对象
        Document document = Jsoup.parse(new URL(url), 30000);

        //获取的一篇文章内容总和
        String subContent = "";

        //2.根据标签中class属性获得Element对象
        //获取class = index-module_mediaWrap_213jB的div标签中的img标签内容  ---> 文章图片内容
        Elements imgs = document.getElementsByClass("index-module_mediaWrap_213jB ");

        //3.获取标签中的内容
        for(Element e : imgs){
            String img = e.getElementsByTag("static/img").attr("src");
            newsImg = new NewsImg();
            newsImg.setImgUrl(img);
            newsImg.setNewsId(newsId);
            newsImgList.add(newsImg);
        }

    return newsImgList;
    }

}
