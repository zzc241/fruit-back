package com.jhc.fruit.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.jhc.fruit.dao.TaskDao;
import com.jhc.fruit.entity.Task;
import com.jhc.fruit.entity.vo.TaskVo;
import com.jhc.fruit.service.EmpGroupService;
import com.jhc.fruit.service.EmployeeService;
import com.jhc.fruit.service.TaskService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


@Slf4j
@Service
public class TaskServiceImpl extends ServiceImpl<TaskDao, Task> implements TaskService {

    @Autowired
    private TaskDao taskDao;

}
