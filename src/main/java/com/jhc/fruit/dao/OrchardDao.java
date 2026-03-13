package com.jhc.fruit.dao;

import com.jhc.fruit.entity.OrchardPoint;
import org.apache.ibatis.annotations.Mapper;
import com.jhc.fruit.entity.Orchard;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
@Mapper
public interface OrchardDao extends BaseMapper<Orchard> {

    void savePoints(Long orchardId, String points);

    OrchardPoint getPoints(Long orchardId);
}
