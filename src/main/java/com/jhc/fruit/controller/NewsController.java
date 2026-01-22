package com.jhc.fruit.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.jhc.fruit.common.NewsUtil;
import com.jhc.fruit.common.R;
import com.jhc.fruit.entity.Employee;
import com.jhc.fruit.entity.FruitTree;
import com.jhc.fruit.entity.News;
import com.jhc.fruit.entity.NewsImg;
import com.jhc.fruit.service.impl.NewsImgServiceImpl;
import com.jhc.fruit.service.impl.NewsServiceImpl;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * @author ：lcy
 * @description：农业新闻相关资讯接口
 * @date ：2022/5/19 0019 20:29
 */
@RequestMapping("/news")
@RestController
@Api(tags = "农业资讯管理接口")
public class NewsController {
    @Autowired
    NewsServiceImpl newsServiceImpl;

    @Autowired
    NewsImgServiceImpl newsImgServiceImpl;

    @PostMapping
    @ApiOperation("从网上爬取新闻数据并存储到数据库中")
    public R<String> saveNews() throws Exception{
        List<News> newsList = NewsUtil.getNews();

        for (News n: newsList) {
            newsServiceImpl.save(n);
            Long newsId = n.getId();
            String url = n.getUrl();
            ArrayList<NewsImg> imgByTitle = NewsUtil.getImgByTitle(newsId, url);
            for(NewsImg newsImg: imgByTitle){
                newsImgServiceImpl.save(newsImg);
            }
        }
        return R.success("获取新闻成功");
    }

    @GetMapping
    @ApiOperation("获取百科资讯中的内容")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "page",value = "当前页码"),
            @ApiImplicitParam(name = "pageSize",value = "最大页码"),
            @ApiImplicitParam(name = "keywords", value = "关键字搜索,如果用户没输入不影响")
    })
    public R page(int page,int pageSize,String keywords) throws Exception{
        Page<News> pageInfo = new Page(page,pageSize);
        //创建条件构造器
        LambdaQueryWrapper<News> lambdaQueryWrapper = new LambdaQueryWrapper<>();
        lambdaQueryWrapper.like(StringUtils.isNotEmpty(keywords),News::getTitle,keywords);
        newsServiceImpl.page(pageInfo,lambdaQueryWrapper);
        return R.success(pageInfo);
    }

    /**
     * 根据id查询果树信息
     * @param id
     * @return
     */
    @GetMapping("/{id}")
    @ApiOperation("根据id查询果树信息，用于修改果树时回溯信息")
    public R<News> getById(@PathVariable Long id){
        News someonNew = newsServiceImpl.getById(id);
        if(someonNew != null){
            return R.success(someonNew);
        }
        return R.error("没有查询到对应果树信息");
    }

    @PutMapping("/{id}")
    @ApiOperation("新闻点赞方法")
    @ApiImplicitParam(name = "id",value = "用户点赞的新闻")
    public R<String> clickAgreement(@PathVariable Long id){
        //1.查询出该新闻当前点赞数
        News news = newsServiceImpl.getById(id);
        //2.点赞数+1
        news.setAgreement(news.getAgreement() + 1);
        //3.修改到数据库当中
        boolean b = newsServiceImpl.updateById(news);
        if(!b){
            return R.error("点赞失败");
        }
        return R.success("点赞成功");
    }
}
