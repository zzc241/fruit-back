package com.jhc.fruit.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.jhc.fruit.entity.WeatherInfo;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface WeatherDao extends BaseMapper<WeatherInfo> {
}
