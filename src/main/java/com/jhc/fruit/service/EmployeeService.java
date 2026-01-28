package com.jhc.fruit.service;

import java.util.List;
import java.util.Map;
import java.util.Set;

import com.baomidou.mybatisplus.extension.service.IService;
import com.jhc.fruit.entity.Employee;

public interface EmployeeService extends IService<Employee> {

    public List<Employee> getWorkers();

    public String getEmpNameById(Long empId);
    public Long getEmpIdByName(String empName);
    public Map<Long, String> getEmpNameMapByIds(Set<Long> empIds);
}
