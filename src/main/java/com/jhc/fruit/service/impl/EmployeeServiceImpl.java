package com.jhc.fruit.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.jhc.fruit.dao.EmployeeDao;
import com.jhc.fruit.entity.Employee;
import com.jhc.fruit.service.EmployeeService;

import java.util.List;

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
}
