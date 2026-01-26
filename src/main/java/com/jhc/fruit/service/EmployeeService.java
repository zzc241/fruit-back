package com.jhc.fruit.service;

import java.util.List;

import com.baomidou.mybatisplus.extension.service.IService;
import com.jhc.fruit.entity.Employee;

public interface EmployeeService extends IService<Employee> {

    public List<Employee> getWorkers();

    public String getEmpNameById(Long empId);
    public Long getEmpIdByName(String empName);
}
