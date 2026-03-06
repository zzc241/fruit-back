package com.jhc.fruit.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.jhc.fruit.entity.EmpGroup;
import com.jhc.fruit.entity.vo.TaskVo;

import java.util.List;
import java.util.Map;
import java.util.Set;

public interface EmpGroupService extends IService<EmpGroup> {
    List<Long> getEmpId(Long groupId);
    boolean deleteGroup(Long id);
//    List<Map<String,String>> getGroupByContent(Long groupId);
//    boolean update(EmpGroup empGroup);
    public List<EmpGroup> getGroupsByEmp(Long empId);
//    public List<Map<String,String>> getGroupByContentEmp(Long empId);
//    public List<EmpGroup> getGroupsByContentEmp(EmpGroup empGroup);
    Long getGroupIdByName(String groupName);

    String getGroupNameByGroupId(Long id);
    public String getGroupNameById(Long groupId);
    public Map<Long, String> getGroupNameMapByIds(Set<Long> groupIds);

    public Long saveEmpGroup(List<Long> empIds , EmpGroup empGroup);
}
