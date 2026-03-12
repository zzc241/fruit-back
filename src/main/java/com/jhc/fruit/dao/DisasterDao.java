package com.jhc.fruit.dao;

import org.apache.ibatis.annotations.Mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.jhc.fruit.entity.Disaster;

import java.util.List;

@Mapper
public interface DisasterDao extends BaseMapper<Disaster> {
    List<String> selectType(Long orchardId, Long typeId);

    Integer getTypeCount(Long orchardId, String type);

    Integer getTypeCountArea(Long orchardId, String type, Long areaId);
}
