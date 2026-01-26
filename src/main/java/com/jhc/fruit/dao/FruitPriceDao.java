package com.jhc.fruit.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.jhc.fruit.entity.FruitPrice;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface FruitPriceDao extends BaseMapper<FruitPrice> {
}