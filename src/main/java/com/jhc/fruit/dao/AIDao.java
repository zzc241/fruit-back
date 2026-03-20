package com.jhc.fruit.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.jhc.fruit.entity.AIWeeds;
import com.jhc.fruit.entity.Analyze;
import com.jhc.fruit.entity.ApiResponse;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface AIDao extends BaseMapper<AIWeeds.WeedDetection> {

    void save(List<AIWeeds.WeedDetection> detections, String originUrl, String outputUrl);

    String getCityByOrchardId(Long orchardId);

    void saveAnalyze(Analyze analyze);

    List<Analyze> getAnalyze(Long orchardId, Long areaId);
}
