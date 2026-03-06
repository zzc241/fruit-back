package com.jhc.fruit.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.jhc.fruit.dao.FruitTypeDao;
import com.jhc.fruit.entity.FruitType;
import com.jhc.fruit.service.FruitTypeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @author ：lcy
 * @description：TODO
 * @date ：2022/6/3 0003 16:03
 */
@Service
public class FruitTypeServiceImpl extends ServiceImpl<FruitTypeDao, FruitType> implements FruitTypeService {

    @Autowired
    private FruitTypeDao fruitTypeDao;

    public String getTypeById(Long id){
        fruitTypeDao.getTypeById(id);
        return fruitTypeDao.getTypeById(id);
    }
    public Long getIdByType(String type){
        LambdaQueryWrapper<FruitType> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(FruitType::getType, type);
        FruitType fruitType = fruitTypeDao.selectOne(queryWrapper);
        Long typeId = fruitType.getId();
        return typeId;

    }
}
