package com.jhc.fruit.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.jhc.fruit.dao.AreaDao;
import com.jhc.fruit.entity.Area;
import com.jhc.fruit.entity.vo.AreaVo;
import com.jhc.fruit.service.AreaService;


import com.jhc.fruit.service.FruitTreeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author ：lcy
 * @description：TODO
 * @date ：2022/5/10 0010 8:30
 */
@Service
public class AreaServiceImpl extends ServiceImpl<AreaDao, Area> implements AreaService {
    @Autowired
    private AreaDao areaDao;
    //这里禁用fruitTreeService，否则会导致依赖循环引用
//    @Autowired
//    private FruitTreeService fruitTreeService;

    @Override
    public Page<AreaVo> page(Page<AreaVo> page) {
        Page<AreaVo> pageInfo = areaDao.page(page);
        return pageInfo;
    }


    @Override
    public boolean updateById(Area area) {
        int i = areaDao.updateById(area);
        if(i == 1){
            return true;
        }
        return false;
    }

    @Override
    public List<AreaVo> getPageInfo(Page pageInfo, String name) {
        List<AreaVo> areaInfo = areaDao.getPageInfo(pageInfo, name);
        return areaInfo;
    }

    @Override
    public List<String> getTypeById() {
        List<String> type = areaDao.getTypeById();
        return type;
    }

    @Override
    public List<Area> getAreaByType(String type) {
        List<Area> areaByType = areaDao.getAreaByType(type);
        return areaByType;
    }

    @Override
    public List<Integer> getId() {
        List<Integer> idList = areaDao.getId();
        return idList;
    }

    @Override
    public Area getById(Integer id) {
        Area area = areaDao.getById(id);
        return area;
    }

//    在fruitType表通过id查找type品类
    @Override
    public Long getIdByType(String type) {
        Long typeId = areaDao.getIdByType(type);
        return typeId;
    }

    @Override
    public String getAreaNameById(Long areaId){
        LambdaQueryWrapper<Area> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(Area::getId, areaId);
        Area area = areaDao.selectOne(queryWrapper);

        String areaName = area.getName();
        if(areaName == null){
            return "DefaultArea";
        }
        return areaName;
    }
    public Long getAreaIdByName(String areaName){
        LambdaQueryWrapper<Area> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(Area::getName, areaName);
        Area area = areaDao.selectOne(queryWrapper);
        Long areaId = area.getId();
        return areaId;
    }

    public List<Area> selectByOrchardId(Long orchardId){
        LambdaQueryWrapper<Area> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(Area::getOrchardId, orchardId);
        List<Area> areaList = areaDao.selectList(queryWrapper);
        return areaList;
    }
    public Long getAreaIdByEmpId (Long empId){
        LambdaQueryWrapper<Area> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(Area::getEmpId, empId);
        Area area = areaDao.selectOne(queryWrapper);
        if(area == null){
            return null;
        }
        Long areaId = area.getId();
        if(areaId == null){
            return null;
        }
        return areaId;
    }

    public void removeArea(Long id) {
        areaDao.removeIncludeTree(id);
        areaDao.deleteById(id);
    }
}
