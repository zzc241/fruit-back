package com.jhc.fruit.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.jhc.fruit.entity.Area;
import com.jhc.fruit.entity.PickInfo;
import org.apache.ibatis.annotations.Select;

import java.util.List;

public interface AreaService extends IService<Area> {
    public List<Area> getPageInfo(Page pageInfo, String name);
    public List<String> getTypeById();
    public List<Integer> getId();
    public List<Area> getAreaByType(String type);
    public Area getById(Integer id);
    public  Long getIdByType(String type);
    public boolean updateById(Area area);
    public Page<Area> page(Page<Area> page);
}
