package com.jhc.fruit.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.jhc.fruit.entity.PickInfo;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import java.util.List;

@Mapper
public interface PickInfoDao extends BaseMapper<PickInfo> {
    public List<PickInfo> getMine(Long empId);
    public Boolean getIsFinish(Long infoId);
    public PickInfo getById(Long id);
    @Select("SELECT id,title,content,create_time,update_time,create_user,update_user,emp_id,is_read,is_finish FROM pick_info ")
    public Page<PickInfo> page(Page<PickInfo> page);
}
