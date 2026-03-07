package com.jhc.fruit.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.jhc.fruit.entity.EmpGroup;
import com.jhc.fruit.entity.Task;

import java.util.List;
import java.util.Map;
import java.util.Set;

public interface EmpGroupService extends IService<EmpGroup> {

    public Map<Long, String> getGroupNameMapByIds(Set<Long> groupIds);

    public Long saveEmpGroup(List<Long> empIds , EmpGroup empGroup);

    public List<Task> getTasks(Long empId);
}
