package com.jhc.fruit.controller;

import com.jhc.fruit.common.R;
import com.jhc.fruit.entity.Task;
import com.jhc.fruit.entity.vo.TaskVo;
import com.jhc.fruit.service.TaskService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.apache.ibatis.annotations.Delete;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

/**
 * @author ：zyr
 * &#064;description  ：任务相关接口
 * &#064;date  ：2026/1/26
 */
@Slf4j
@RestController
@RequestMapping("/task")
@Api(tags = "任务管理接口")
public class TaskController {

    @Autowired
    private TaskService taskService;

    /*新增任务*/
    @ApiOperation(value = "新增任务")
    @PostMapping("/add")
    public R<String> addTask(@RequestBody TaskVo taskVo) {
        taskService.addTask(taskVo);
        return R.success("新增成功");
    }

    @ApiOperation(value = "修改任务")
    @PostMapping("update")
    public R<String> updateTask(@RequestBody TaskVo taskVo) {
        taskService.updateTask(taskVo);
        return R.success("修改成功");
    }

    @ApiOperation(value = "根据id删除任务")
    @DeleteMapping("/delete/{id}")
    public R<String> deleteTask(@PathVariable Long id) {
        taskService.removeById(id);
        return R.success("删除成功");
    }

    @ApiOperation(value = "批量删除任务")
    @DeleteMapping("/deleteList")
    public R<String> deleteTask(@RequestBody List<Long> taskIds) {
        taskService.removeByList(taskIds);
        return R.success("删除成功");
    }

    @ApiOperation(value = "今日发布的任务")
    @GetMapping("/todayTask")
    public R<List<TaskVo>> todayTask() {
        List<TaskVo> tasks = taskService.todayTask();
        return R.success(tasks);
    }

    @GetMapping("/getById/{id}")
    @ApiOperation(value = "根据id查询任务")
    public R<TaskVo> getTaskById(@PathVariable Long id) {
        TaskVo taskVo = taskService.getById(id);
        return R.success(taskVo);
    }

    @PostMapping("/query")
    @ApiOperation(value = "条件查询")
    public R<List<TaskVo>> queryTasks(@RequestBody TaskVo taskVo) {
        try{
            List<TaskVo> taskList = taskService.queryTasks(taskVo);
            return R.success(taskList);
        }catch (Exception e){
            log.error(e.getMessage());
            return R.error("查询失败");
        }
    }
}
