package com.jhc.fruit.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.jhc.fruit.entity.EmpGroup;

import java.util.List;
import java.util.Map;
import java.util.Set;

public interface EmpGroupService extends IService<EmpGroup> {

    public Map<Long, String> getGroupNameMapByIds(Set<Long> groupIds);

    Boolean addEmployee(List<Long> empIds, Long groupId);
}
