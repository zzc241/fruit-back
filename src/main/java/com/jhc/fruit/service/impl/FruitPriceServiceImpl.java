package com.jhc.fruit.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.jhc.fruit.common.PriceUtil;
import com.jhc.fruit.common.R;
import com.jhc.fruit.dao.FruitPriceDao;
import com.jhc.fruit.entity.FruitPrice;
import com.jhc.fruit.service.FruitPriceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;

@Service
@Transactional
public class FruitPriceServiceImpl extends ServiceImpl<FruitPriceDao, FruitPrice> implements FruitPriceService {

    @Autowired
    private FruitPriceDao fruitPriceDao;

    @Override
    public List<FruitPrice> getFruitPrice(String fruitName) {
        // 1. 爬取实时价格数据
        List<FruitPrice> crawlList = PriceUtil.getFruitPrice(fruitName);
        // 2. 遍历爬取的数据，逐个判断是否需要入库
        for (FruitPrice fp : crawlList) {
            //根据水果名+价格时间+地区,查询当天是否有爬取过对应数据
            LambdaQueryWrapper<FruitPrice> queryWrapper = new LambdaQueryWrapper<>();
            queryWrapper.eq(FruitPrice::getFruitName, fp.getFruitName())
                    .eq(FruitPrice::getPriceDate, fp.getPriceDate())
                    .eq(FruitPrice::getOrigin, fp.getOrigin());

            // 查询数据库中是否已有该条数据
            FruitPrice existFp = fruitPriceDao.selectOne(queryWrapper);

            if (existFp == null) {
                //无数据 》 插入新数据
                fruitPriceDao.insert(fp);
            } else {
                //有数据 》 比对价格是否变化,有变化再更新数据
                if (!existFp.getPrice().equals(fp.getPrice())) {
                    // 价格变化 》 更新数据
                    //保留价格变更记录，插入新数据并标记更新时间
                    fp.setUpdateTime(LocalDateTime.now());
                    fruitPriceDao.insert(fp);
                }
                // 价格无变化 》 直接跳过，不做任何操作
            }
        }
        return crawlList;
    }

    @Override
    public List<FruitPrice> selectOfAddress(String address) {
        LambdaQueryWrapper<FruitPrice> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(FruitPrice::getOrigin, address);
        List<FruitPrice> crawlList = fruitPriceDao.selectList(queryWrapper);

        return crawlList;
    }

}