package com.jhc.fruit.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.jhc.fruit.common.R;
import com.jhc.fruit.entity.Task;
import com.jhc.fruit.entity.vo.OwnTaskVo;
import com.jhc.fruit.entity.vo.TaskStatisticsVO;
import com.jhc.fruit.entity.vo.TaskVo;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

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

    TaskVo taskToVo(Task task);

    List<TaskVo> queryTasksBackend(TaskVo taskVo);

   List<TaskVo> getByOrchardId(Long OrchardId);

   List<TaskVo> getByAreaId(Long areaId);

    R<OwnTaskVo> getOwnTask(Long empId);

    R<List<TaskVo>> getMyTask(Long orchardId, Long empId);

    R<List<TaskVo>> getTypeTask(Long orchardId, String type, Long empId);

    R<List<Map<String, Integer>>> getTaskType(Long empId);

    R<List<TaskVo>> getReview(Long orchardId);

    R<String> reviewOk(List<Long> taskIds);

    R<String> reviewFaile(List<Long> taskIds);

    R<Map<String, Double>> countTaskByType();

    R<TaskStatisticsVO> countByStatus();

    boolean deleteTask(Long id);
}
