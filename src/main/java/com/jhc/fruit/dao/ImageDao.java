package com.jhc.fruit.dao;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;

@Mapper
public interface ImageDao extends BaseMapper<String>{
    public List<String> getRawImage();

    public List<String> getHandledImage();
}
