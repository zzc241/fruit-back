package com.jhc.fruit.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.jhc.fruit.dao.NewsDao;
import com.jhc.fruit.entity.News;
import com.jhc.fruit.service.NewsService;
import org.springframework.stereotype.Service;

/**
 * @author ：lcy
 * @description：TODO
 * @date ：2022/5/19 0019 20:28
 */
@Service
public class NewsServiceImpl extends ServiceImpl<NewsDao, News> implements NewsService {
}
