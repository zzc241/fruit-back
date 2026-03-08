package com.jhc.fruit.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.jhc.fruit.entity.Task;
import com.jhc.fruit.entity.vo.TaskVo;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author zyr
 * &#064;date  2026/1/26
 */
@Service
public interface TaskService extends IService<Task> {

   Long addTask(TaskVo taskVo);

   void updateTask(TaskVo taskVo);

   void removeByList(List<Long> taskIds);

   List<TaskVo> todayTask();

   List<TaskVo> queryTasks(TaskVo tasksVo);

   List<TaskVo> queryTasksBackend(TaskVo taskVo);

   List<TaskVo> getByOrchardId(Long OrchardId);

}
