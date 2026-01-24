package com.jhc.fruit.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.jhc.fruit.entity.FruitType;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface FruitTypeDao extends BaseMapper<FruitType> {

    public String getTypeById(Long id);
}
