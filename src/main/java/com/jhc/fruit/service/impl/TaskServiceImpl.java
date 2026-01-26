package com.jhc.fruit.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.jhc.fruit.dao.TaskDao;
import com.jhc.fruit.entity.Task;
import com.jhc.fruit.service.TaskService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Slf4j
@Service
public class TaskServiceImpl extends ServiceImpl<TaskDao, Task> implements TaskService {

    @Autowired
    private TaskDao taskDao;

    @Override
    public void addTask(Task task) {
        try {
            taskDao.insert(task);
        }catch (Exception e){
            log.error(e.getMessage());
            throw new RuntimeException(e);
        }

    }

    @Override
    public void updateTask(Task task) {
        try {
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

    /*根据发布日期查询任务*/
    @Override
    public List<Task> selectByPostTime(LocalDateTime start, LocalDateTime end) {
        // 构建Lambda条件构造器
        LambdaQueryWrapper<Task> queryWrapper = new LambdaQueryWrapper<Task>()
                // 添加时间范围条件
                // 大于等于开始时间
                .ge(Task::getPostTime, start)
                // 小于等于结束时间
                .le(Task::getPostTime, end);

        // 执行查询
        List<Task> tasks = this.list(queryWrapper);
        return tasks;
    }
}
