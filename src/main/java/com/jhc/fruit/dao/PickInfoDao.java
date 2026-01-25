package com.jhc.fruit.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.jhc.fruit.entity.PickInfo;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
@Mapper
/**
 * @author ：zzc
 * @description：impl已经全部变成mybatis plus的通用方法，所以这个接口基本全部方法都不需要重写
 * @date ：2026/1/23
 */
public interface PickInfoDao extends BaseMapper<PickInfo> {
}
