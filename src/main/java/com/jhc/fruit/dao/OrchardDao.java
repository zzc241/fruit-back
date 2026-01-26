package com.jhc.fruit.dao;

import org.apache.ibatis.annotations.Mapper;
import com.jhc.fruit.entity.Orchard;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
@Mapper
public interface OrchardDao extends BaseMapper<Orchard> {
    
}
