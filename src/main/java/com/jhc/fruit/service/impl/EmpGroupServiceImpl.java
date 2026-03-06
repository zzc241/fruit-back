package com.jhc.fruit.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.jhc.fruit.dao.EmpGroupDao;
import com.jhc.fruit.dao.EmployeeDao;
import com.jhc.fruit.entity.EmpGroup;
import com.jhc.fruit.entity.Employee;
import com.jhc.fruit.entity.vo.TaskVo;
import com.jhc.fruit.service.EmpGroupService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
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

    @Override
    public Map<Long, String> getGroupNameMapByIds(Set<Long> groupIds) {
        return null;
    }

    @Override
    public Boolean addEmployee(List<Long> empIds, Long groupId) {
        try {
            for (Long empId : empIds) {
                empGroupDao.addEmployee(empId, groupId);
            }
            return true;
        }catch (Exception e){
            e.printStackTrace();
            return false;
        }
    }


    @Override
    public Long saveEmpGroup(List<Long> empIds , EmpGroup empGroup) {
        this.save(empGroup);
        Long groupId = empGroup.getGroupId();
        for(Long empId : empIds){


        }
        return groupId;

    }

}
