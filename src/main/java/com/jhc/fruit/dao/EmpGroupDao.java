package com.jhc.fruit.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.jhc.fruit.entity.EmpGroup;
import com.jhc.fruit.entity.Task;

import org.apache.ibatis.annotations.Mapper;

import java.util.List;
import java.util.Map;

@Mapper
public interface EmpGroupDao extends BaseMapper<EmpGroup> {
    public List<Long> getEmpId(Long groupId);
    public int deleteGroup(Long id);
//    public List<Map<String,String>> getGroupByContent(Long groupId);
//    public int update(EmpGroup empGroup);
    public List<EmpGroup> getGroupsByEmp(Long empId);
//    public List<Map<String,String>> getGroupByContentEmp(Long empId);
    public List<EmpGroup> getGroupsByContentEmp(EmpGroup empGroup);

    Long getGroupIdByName(String groupName);

    String getGroupNameByGroupId(Long id);

    void addEmployee(Long empId, Long groupId);

    List<Task> getTasksByEmpId(Long empId);
}
