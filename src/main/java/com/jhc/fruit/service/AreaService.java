package com.jhc.fruit.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.jhc.fruit.common.R;
import com.jhc.fruit.entity.Area;
import com.jhc.fruit.entity.vo.AreaVo;

import java.util.List;

public interface AreaService extends IService<Area> {
    public List<AreaVo> getPageInfo(Page pageInfo, String name);
    public List<String> getTypeById();
    public List<Integer> getId();
    public List<Area> getAreaByType(String type);
    public Area getById(Integer id);
    public  Long getIdByType(String type);
    public boolean updateById(Area area);
    public Page<AreaVo> page(Page<AreaVo> page);


    public String getAreaNameById(Long areaId);
    public Long getAreaIdByName(String areaName);

    public List<AreaVo> selectByOrchardId(Long orchardId);
    public Long getAreaIdByEmpId (Long empId);

    void removeArea(Long id);

    List<String> listName(Long id);

    R<String> addEmployee(Long empId, Long areaId);

    AreaVo areaToVo(Area area);
}
