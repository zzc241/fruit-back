package com.jhc.fruit.service.impl;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.jhc.fruit.dao.AreaDao;
import com.jhc.fruit.entity.Area;
import com.jhc.fruit.entity.PickInfo;
import com.jhc.fruit.service.AreaService;
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

    @Override
    public Page<Area> page(Page<Area> page) {
        Page<Area> pageInfo = areaDao.page(page);
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
    public List<Area> getPageInfo(Page pageInfo, String name) {
        List<Area> areaInfo = areaDao.getPageInfo(pageInfo, name);
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

    @Override
    public Long getIdByType(String type) {
        Long typeId = areaDao.getIdByType(type);
        return typeId;
    }
}
