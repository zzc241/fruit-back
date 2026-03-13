package com.jhc.fruit.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.jhc.fruit.entity.Area;
import com.jhc.fruit.entity.AnnounceInfo;
import com.jhc.fruit.entity.Employee;
import com.jhc.fruit.entity.vo.AreaVo;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import java.util.List;

@Mapper
public interface AreaDao extends BaseMapper<Area> {
    public List<String> getTypeById();
    public  Long getIdByType(String type);
    public List<Integer> getId();
    public List<Area> getAreaByType(String type);
    public List<AreaVo> getPageInfo(Page pageInfo, String name);
    public Area getById(Integer id);
    // public int updateById(Area area);
    @Select("SELECT id,name,size,type_id,create_time,update_time,description,create_user,update_user,emp_id  FROM area_info")
    public Page<AreaVo> page(Page<AreaVo> page);

    void removeIncludeTree(Long id);

    String getEmpName(Long empId);

    String getAreaNameById(Long areaId);

    String getTypeName(Long typeId);

    String getOrchardName(Long orchardId);

    List<Employee> selectEmp(Long empGroup);
}
