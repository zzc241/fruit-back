package com.jhc.fruit.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.jhc.fruit.dao.EmpGroupDao;
import com.jhc.fruit.dao.EmployeeDao;
import com.jhc.fruit.entity.EmpGroup;
import com.jhc.fruit.entity.Employee;
import com.jhc.fruit.service.EmpGroupService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.HashMap;
import java.util.stream.Collectors;

/**
 * @author ：lcy
 * @description：TODO
 * @date ：2022/6/13 0013 9:26
 */
@Service
public class EmpGroupServiceImpl extends ServiceImpl<EmpGroupDao, EmpGroup> implements EmpGroupService {

    @Autowired
    private EmpGroupDao empGroupDao;

    @Autowired
    private EmployeeDao employeeDao;

//    @Override
//    public List<Map<String, String>> getGroupByContentEmp(Long empId) {
//        List<Map<String, String>> groupByContentEmp = empGroupDao.getGroupByContentEmp(empId);
//        return groupByContentEmp;
//    }

/*
    @Override
    public List<EmpGroup> getGroupsByContentEmp(EmpGroup empGroup) {
        List<EmpGroup> groupByContentEmp = empGroupDao.getGroupsByContentEmp(empGroup);
        for(EmpGroup emp: groupByContentEmp){
            Employee employee = employeeDao.selectById(emp.getEmpId());
            emp.setEmployee(employee);
        }
        return groupByContentEmp;
    }
*/

    @Override
    public List<EmpGroup> getGroupsByEmp(Long empId) {
        List<EmpGroup> groupsByEmp = empGroupDao.getGroupsByEmp(empId);
        return groupsByEmp;
    }

    @Override
    public Long getGroupIdByName(String groupName) {
        Long groupId = empGroupDao.getGroupIdByName(groupName);
        return groupId;
    }

    @Override
    public String getGroupNameByGroupId(Long id) {
        String name = empGroupDao.getGroupNameByGroupId(id);
        return name;
    }

    @Override
    public Map<Long, String> getGroupNameMapByIds(Set<Long> groupIds) {
        if (groupIds == null || groupIds.isEmpty()) {
            return new HashMap<>();
        }
        LambdaQueryWrapper<EmpGroup> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.in(EmpGroup::getGroupId, groupIds);
        List<EmpGroup> empGroups = list(queryWrapper);
        return empGroups.stream()
                .collect(Collectors.toMap(EmpGroup::getGroupId, EmpGroup::getGroupName, (existing, replacement) -> existing));
    }

    @Override
    public String getGroupNameById(Long id) {
        return getGroupNameByGroupId(id);
    }

/*    @Override
    public boolean update(EmpGroup empGroup) {
        int update = empGroupDao.update(empGroup);
        if(update == 1){
            return true;
        }
        return false;
    }*/

/*    @Override
    public List<Map<String, String>> getGroupByContent(Long groupId) {
        List<Map<String, String>> groupByContent = empGroupDao.getGroupByContent(groupId);
        return groupByContent;
    }*/

    @Override
    public List<Long> getEmpId(Long groupId) {
        List<Long> empId = empGroupDao.getEmpId(groupId);
        return empId;
    }

    @Override
    public boolean deleteGroup(Long id) {
        int isDelete = empGroupDao.deleteGroup(id);
        if(isDelete == 1){
            return true;
        }
        return false;
    }


}