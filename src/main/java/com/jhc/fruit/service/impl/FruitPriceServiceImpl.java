package com.jhc.fruit.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.jhc.fruit.dao.FruitPriceDao;
import com.jhc.fruit.entity.FruitPrice;
import com.jhc.fruit.service.FruitPriceService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class FruitPriceServiceImpl extends ServiceImpl<FruitPriceDao, FruitPrice> implements FruitPriceService {
}