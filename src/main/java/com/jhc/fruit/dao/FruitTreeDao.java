package com.jhc.fruit.dao;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.jhc.fruit.entity.FruitTree;
import com.jhc.fruit.entity.FruitType;
import com.jhc.fruit.entity.PickInfo;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import java.util.List;
import java.util.Map;

@Mapper
public interface FruitTreeDao extends BaseMapper<FruitTree> {
    /**
     * 由于水果种类不能以种类id形式展露【可读性太差】
     */
    public List<FruitTree> pageSelect(Page<FruitTree> page, String type);
    public List<Integer> getAreaId();
    public Long getTypeId(int areaId);
    public List<FruitType> showType();
    public FruitTree getById(Long id);
    public List<FruitTree> selectByAreaId(int areaId);
    public List<FruitTree> sortByRipeCount(int areaId);
    public List<FruitTree> sortByCount(int areaId);
    public FruitTree selectBy2Id(Long id);
    public int updateById(FruitTree fruitTree);
    @Select("SELECT id,type,count_num,ripe_num,img,area_id FROM fruit_tree")
    public Page<FruitTree> page(Page<FruitTree> page);
}
