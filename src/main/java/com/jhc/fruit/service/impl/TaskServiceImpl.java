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

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Slf4j
@Service
public class TaskServiceImpl extends ServiceImpl<TaskDao, Task> implements TaskService {

    @Autowired
    private TaskDao taskDao;
    @Autowired
    private EmployeeService employeeService;
    @Autowired
    private EmpGroupService empGroupService;

    /*新增任务*/
    @Override
    public void addTask(TaskVo taskVo) {
        try {
            Task task = voToTask(taskVo);
            //设置创建时间
            task.setCreateTime(LocalDateTime.now());
            taskDao.insert(task);
        }catch (Exception e){
            log.error(e.getMessage());
            throw new RuntimeException(e);
        }

    }

    /*更新任务*/
    @Override
    public void updateTask(TaskVo taskVo) {
        try {
            Task task = voToTask(taskVo);
            taskDao.updateById(task);
        }catch (Exception e){
            log.error(e.getMessage());
            throw new RuntimeException(e);
        }
    }

    /*批量删除*/
    @Override
    public void removeByList(List<Long> taskIds) {
        try{
            /*调用MybatisPlus自带的删除方法（通过List<Long>id列表）*/
            taskDao.deleteBatchIds(taskIds);
        }catch(Exception e){
            log.error(e.getMessage());
            throw new RuntimeException(e);
        }
    }

    /*根据查询今日任务*/
//    TODO
    @Override
    public List<TaskVo> todayTask() {
        LocalDateTime now = LocalDateTime.now();
        // 今日开始时间：年-月-日 00:00:00
        LocalDateTime start = now.withHour(0).withMinute(0).withSecond(0);
        // 今日结束时间：年-月-日 23:59:59
        LocalDateTime end = now.withHour(23).withMinute(59).withSecond(59);
        // 构建Lambda条件构造器
        LambdaQueryWrapper<Task> queryWrapper = new LambdaQueryWrapper<Task>()
                // 添加时间范围条件
                // 大于等于开始时间
                .ge(Task::getPostTime, start)
                // 小于等于结束时间
                .le(Task::getPostTime, end);

        // 执行查询
        List<Task> tasks = this.list(queryWrapper);
        //转换为vo类
        List<TaskVo> taskVos = tasks.stream()
                                .map(this::taskToVo)
                                .collect(Collectors.toList());
        return taskVos;
    }

//    条件查询
//    TODO
    @Override
    public List<TaskVo> queryTasks(TaskVo taskVo) {
        Task task = voToTask(taskVo);
//当对应条件不为空时，拼接查询条件
        LambdaQueryWrapper<Task> queryWrapper = new LambdaQueryWrapper<>();
        //任务id
        queryWrapper.eq(task.getId() != null, Task::getId, task.getId());
        //创建人id
        queryWrapper.eq(task.getCreatorId() != null, Task::getCreatorId, task.getCreatorId());
        //任务类型
        queryWrapper.eq(task.getTaskType() != null, Task::getTaskType, task.getTaskType());
        //任务范围
        queryWrapper.eq(task.getTaskScope() != null, Task::getTaskScope, task.getTaskScope());
        //任务状态
        queryWrapper.eq(task.getStatus() != null, Task::getStatus, task.getStatus());
        //发布时间
        queryWrapper
                .ge(Task::getPostTime, taskVo.getStart())
                .le(Task::getPostTime, taskVo.getEnd());

        List<Task> tasks = this.list(queryWrapper);

        List<TaskVo> taskVos = tasks.stream()
                .map(this::taskToVo)
                .collect(Collectors.toList());


        return taskVos;
    }

    /*根据id查询*/
    @Override
    public TaskVo getById(Long id) {
        Task task = taskDao.selectById(id);
        TaskVo taskVo = taskToVo(task);
        return taskVo;
    }

    private Task voToTask(TaskVo taskVo) {
        Task task = new Task();
        BeanUtils.copyProperties(taskVo, task);
/*        //从taskVo获取创建人名字，从名字查询用户id
        task.setCreatorId(
                employeeService.getEmpIdByName(
                        taskVo.getUsername()));*/
        //通过组名查询组id
        task.setTaskScope(
                empGroupService.getGroupIdByName(
                        taskVo.getGroupName()));
        return task;
    }

    private TaskVo taskToVo(Task task) {
        TaskVo taskVo = new TaskVo();
        BeanUtils.copyProperties(task, taskVo);
        taskVo.setUsername(
                employeeService.getEmpNameById(
                        task.getCreatorId()));
        taskVo.setGroupName(
                empGroupService.getGroupNameByGroupId(
                        task.getTaskScope()));
        return taskVo;
    }
}
