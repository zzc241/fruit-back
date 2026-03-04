package com.jhc.fruit.service;

import java.util.List;
import java.util.Map;
import java.util.Set;

import com.baomidou.mybatisplus.extension.service.IService;
import com.jhc.fruit.common.R;
import com.jhc.fruit.entity.Employee;
import com.jhc.fruit.entity.vo.loginEmployeeVo;

import javax.servlet.http.HttpServletRequest;

public interface EmployeeService extends IService<Employee> {

    public List<Employee> getWorkers();

    public String getEmpNameById(Long empId);
    public Long getEmpIdByName(String empName);
    public Map<Long, String> getEmpNameMapByIds(Set<Long> empIds);

    //微信登录
    R<Employee> openIdLogin(String code, HttpServletRequest request);

    R<Employee> login(loginEmployeeVo loginEmployeeVo, HttpServletRequest request);

    R<String> deleteList(List<Long> ids);
}
