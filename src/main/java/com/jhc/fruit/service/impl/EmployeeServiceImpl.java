package com.jhc.fruit.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.jhc.fruit.dao.EmployeeDao;
import com.jhc.fruit.entity.Employee;
import com.jhc.fruit.service.EmployeeService;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

/**
 * @author ：lcy
 * @description：TODO
 * @date ：2022/4/28 0028 14:04
 */
@Service
public class EmployeeServiceImpl extends ServiceImpl<EmployeeDao, Employee> implements EmployeeService {

    @Override
    public List<Employee> getWorkers(){
        LambdaQueryWrapper<Employee> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(Employee::getIsAdmin,2);
        List<Employee> workers = list(wrapper);
        if(workers != null){
            return workers;
        }
        return null;
    }

    @Override
    public String getEmpNameById(Long empId){
        LambdaQueryWrapper<Employee> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(Employee::getId, empId);
        Employee employee = getOne(queryWrapper);
        String empName = employee.getName();
        return empName;
    }
    @Override
    public Long getEmpIdByName(String empName){
        LambdaQueryWrapper<Employee> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(Employee::getName, empName);
        Employee employee = getOne(queryWrapper);
        Long empId = employee.getId();
        return empId;
    }
    @Override
    public Map<Long, String> getEmpNameMapByIds(Set<Long> empIds){
        if (empIds == null || empIds.isEmpty()) {
            return new HashMap<>(); // 空集合直接返回空Map
        }
        LambdaQueryWrapper<Employee> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.in(Employee::getId, empIds);
        queryWrapper.select(Employee::getId, Employee::getName);
        List<Employee> employees = list(queryWrapper);
        Map<Long, String> empNameMap = employees.stream().collect(
            Collectors.toMap(
                Employee::getId, 
                Employee::getName,
                (existingValue, newValue) -> existingValue));
        return empNameMap;
    }
}
