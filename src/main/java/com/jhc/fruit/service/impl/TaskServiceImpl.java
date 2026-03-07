package com.jhc.fruit.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.jhc.fruit.dao.TaskDao;
import com.jhc.fruit.entity.EmpGroup;
import com.jhc.fruit.entity.Task;
import com.jhc.fruit.entity.TaskImg;
import com.jhc.fruit.entity.vo.TaskVo;
import com.jhc.fruit.service.EmpGroupService;
import com.jhc.fruit.service.EmployeeService;
import com.jhc.fruit.service.OrchardService;
import com.jhc.fruit.service.TaskImgService;
import com.jhc.fruit.service.TaskService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Iterator;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
public class TaskServiceImpl extends ServiceImpl<TaskDao, Task> implements TaskService {

    @Autowired
    private TaskDao taskDao;
    @Autowired
    private EmployeeService employeeService;
    @Autowired
    private TaskImgService taskImgService;
    @Autowired
    private OrchardService orchardService;
    @Autowired
    private EmpGroupService empGroupService;

    /*新增任务*/
    @Override
    public Long addTask(TaskVo taskVo) {
        try {
            Task task = voToTask(taskVo);
            //设置创建时间
            this.save(task);
            Long id = task.getId();
            List<String> img = taskVo.getImgsURL();
            Iterator<String> iterator = img.iterator();
            while (iterator.hasNext()) {
                String imgUrl = iterator.next();
                TaskImg taskImg = new TaskImg();
                taskImg.setImg(imgUrl);
                taskImg.setTaskId(id);
                taskImgService.addTaskImg(taskImg);
            }
            //判断前端是否传回员工组id
            if(taskVo.getEmpIds() != null){
                //如果返回员工组id，则创建一个groupId
                EmpGroup empGroup = new EmpGroup();
                empGroup.setGroupName(taskVo.getGroupName());
                empGroup.setDescription(taskVo.getGroupDescription());
                Long groupId = empGroupService.saveEmpGroup(taskVo.getEmpIds() , empGroup);
                task.setGroupId(groupId);
                return groupId;
            }
            return taskVo.getOrchardId();
            
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
            this.updateById(task);
            List<String> img = taskVo.getImgsURL();
            Iterator<String> iterator = img.iterator();
            Long id = task.getId();
            taskImgService.removeByTaskId(id);
            while (iterator.hasNext()) {
                String imgUrl = iterator.next();
                TaskImg taskImg = new TaskImg();
                taskImg.setImg(imgUrl);
                taskImg.setTaskId(task.getId());
                taskImgService.addTaskImg(taskImg);
            }
        }catch (Exception e){
            log.error(e.getMessage());
            throw new RuntimeException(e);
        }
    }

        /*根据id查询*/
    @Override
    public TaskVo getById(Long id) {
        Task task = taskDao.selectById(id);
        Long taskId = task.getId();
        TaskVo taskVo = taskToVo(task);
        taskVo.setUsername(employeeService.getById(task.getCreateUser()).getUsername());
        taskVo.setOrchardName(orchardService.getOrchardNameById(task.getOrchardId()));
        taskVo.setImgsURL(taskImgService.getImg(taskId));
        return taskVo;
    }


    /*批量删除*/
    @Override
    public void removeByList(List<Long> taskIds) {
        try{
            /*调用MybatisPlus自带的删除方法（通过List<Long>id列表）*/
            taskDao.deleteBatchIds(taskIds);
            Iterator<Long> iterator = taskIds.iterator();
            while (iterator.hasNext()) {
                Long taskId = iterator.next();
                //删除任务图片
                taskImgService.removeByTaskId(taskId);
            }
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
        Iterator<TaskVo> iterator = taskVos.iterator();
        while (iterator.hasNext()) {
            TaskVo task = iterator.next();
            Long taskId = task.getId();
            task.setImgsURL(taskImgService.getImg(taskId));
        }
        return taskVos;
    }

    //    条件查询
    @Override
    public List<TaskVo> queryTasks(TaskVo taskVo) {
        Task task = voToTask(taskVo);
    //当对应条件不为空时，拼接查询条件
        LambdaQueryWrapper<Task> queryWrapper = new LambdaQueryWrapper<>();
        //任务id
        queryWrapper.eq(task.getId() != null, Task::getId, task.getId());
        //创建人id
        queryWrapper.eq(task.getCreatorId() != null, Task::getCreatorId, task.getCreatorId());
        //果园id
        queryWrapper.eq(task.getOrchardId() != null, Task::getOrchardId, task.getOrchardId());
        //任务类型
        queryWrapper.eq(task.getTaskType() != null, Task::getTaskType, task.getTaskType());
        //任务范围
        if(task.getGroupId() != null){
            queryWrapper.eq(Task::getGroupId, task.getGroupId());

        }
        //任务状态
        queryWrapper.eq(task.getStatus() != null, Task::getStatus, task.getStatus());
        //发布时间
        List<Task> tasks = this.list(queryWrapper);

        List<TaskVo> taskVos = tasks.stream()
                .map(this::taskToVo)
                .collect(Collectors.toList());

        Iterator<TaskVo> iterator = taskVos.iterator();
        while(iterator.hasNext()){
            TaskVo taskVoCurr = iterator.next();
            Long taskId = taskVoCurr.getId();
            taskVoCurr.setImgsURL(taskImgService.getImg(taskId));
        }

        return taskVos;
    }

    private Task voToTask(TaskVo taskVo) {
        Task task = new Task();
        BeanUtils.copyProperties(taskVo, task);
    /*        //从taskVo获取创建人名字，从名字查询用户id
        task.setCreatorId(
                employeeService.getEmpIdByName(
                        taskVo.getUsername()));*/
        return task;
    }

    private TaskVo taskToVo(Task task) {
        TaskVo taskVo = new TaskVo();
        BeanUtils.copyProperties(task, taskVo);
        taskVo.setUsername(
                employeeService.getEmpNameById(
                        task.getCreatorId()));
        return taskVo;
    }
}
