package com.jhc.fruit.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.jhc.fruit.entity.FruitTree;
import com.jhc.fruit.entity.FruitType;
import com.jhc.fruit.entity.PickInfo;

import java.util.List;

public interface FruitTreeService extends IService<FruitTree> {
    public List<FruitTree> pageSelect(Page<FruitTree> page,String type);
    public Long getTypeId(int areaId);
    public List<FruitType> showType();
    public FruitTree getById(Long id);
    public List<FruitTree> selectByAreaId(int areaId);
    public List<FruitTree> sortByRipeCount(int areaId);
    public List<FruitTree> sortByCount(int areaId);
    public FruitTree selectBy2Id(Long id);
    public boolean updateById(FruitTree fruitTree);
    public Page<FruitTree> page(Page<FruitTree> page);
}
