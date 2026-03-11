package com.jhc.fruit.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.jhc.fruit.entity.AIWeeds;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface AIDao extends BaseMapper<AIWeeds.WeedDetection> {

    void save(List<AIWeeds.WeedDetection> detections);
}
