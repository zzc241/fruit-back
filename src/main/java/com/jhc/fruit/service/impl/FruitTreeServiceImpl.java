package com.jhc.fruit.service.impl;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.jhc.fruit.dao.FruitTreeDao;
import com.jhc.fruit.entity.FruitTree;
import com.jhc.fruit.entity.FruitType;
import com.jhc.fruit.entity.PickInfo;
import com.jhc.fruit.service.FruitTreeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

/**
 * @author ：lcy
 * @description：TODO
 * @date ：2022/5/17 0017 9:42
 */
@Service
public class FruitTreeServiceImpl extends ServiceImpl<FruitTreeDao, FruitTree> implements FruitTreeService {

    @Autowired
    private FruitTreeDao fruitTreeDao;

    @Override
    public Page<FruitTree> page(Page<FruitTree> page) {
        Page<FruitTree> pageInfo = fruitTreeDao.page(page);
        return pageInfo;
    }

    @Override
    public boolean updateById(FruitTree fruitTree) {
        int i = fruitTreeDao.updateById(fruitTree);
        if(i == 1){
            return true;
        }
        return false;
    }

    @Override
    public FruitTree selectBy2Id(Long id) {
        FruitTree fruitTree = fruitTreeDao.selectBy2Id(id);
        return fruitTree;
    }

    @Override
    public List<FruitTree> pageSelect(Page<FruitTree> pageInfo,String type) {
        List<FruitTree> page = fruitTreeDao.pageSelect(pageInfo,type);
        return page;
    }

    @Override
    public Long getTypeId(int areaId) {
        Long typeId = fruitTreeDao.getTypeId(areaId);
        return typeId;
    }

    @Override
    public List<FruitType> showType() {
        List<FruitType> typeList = fruitTreeDao.showType();
        return typeList;
    }

    @Override
    public FruitTree getById(Long id) {
        FruitTree fruitTree = fruitTreeDao.getById(id);
        return fruitTree;
    }

    @Override
    public List<FruitTree> selectByAreaId(int areaId) {
        List<FruitTree> fruitTreeList = fruitTreeDao.selectByAreaId(areaId);
        return fruitTreeList;
    }

    @Override
    public List<FruitTree> sortByRipeCount(int areaId) {
        List<FruitTree> fruitTrees = fruitTreeDao.sortByRipeCount(areaId);
        return fruitTrees;
    }

    @Override
    public List<FruitTree> sortByCount(int areaId) {
        List<FruitTree> fruitTrees = fruitTreeDao.sortByCount(areaId);
        return fruitTrees;
    }
}
