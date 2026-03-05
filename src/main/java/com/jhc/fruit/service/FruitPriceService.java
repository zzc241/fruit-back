package com.jhc.fruit.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.jhc.fruit.common.R;
import com.jhc.fruit.entity.FruitPrice;

import java.util.List;

public interface FruitPriceService extends IService<FruitPrice> {
    List<FruitPrice> getFruitPrice(String fruitName);

    List<FruitPrice> selectOfAddress(String address);
}