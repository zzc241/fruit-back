package com.jhc.fruit.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.jhc.fruit.entity.Task;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

/**
 * @author zyr
 * &#064;date  2026/1/26
 */

public interface TaskService extends IService<Task> {

    void addTask(Task task);

    void updateTask(Task task);

    void removeByList(List<Long> taskIds);

    List<Task> selectByPostTime(LocalDateTime start, LocalDateTime end);
}
