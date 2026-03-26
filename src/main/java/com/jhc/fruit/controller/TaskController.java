package com.jhc.fruit.controller;

import com.jhc.fruit.common.R;
import com.jhc.fruit.entity.Report;
import com.jhc.fruit.entity.vo.OwnTaskVo;
import com.jhc.fruit.entity.vo.ReportVo;
import com.jhc.fruit.entity.vo.TaskVo;
import com.jhc.fruit.service.TaskService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import java.util.List;
import java.util.Map;

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
    @PostMapping("/update")
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

    @GetMapping("/getById/{OrchardId}")
    @ApiOperation(value = "根据果园id查询任务")
    public R<List<TaskVo>> getTaskByOrchardIdId(@PathVariable Long OrchardId) {
        List<TaskVo> taskVo = taskService.getByOrchardId(OrchardId);
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
    @PostMapping("/queryCreaterId")
    @ApiOperation(value = "通过发布者id条件查询任务")
    public R<List<TaskVo>> queryTasksBackend(@RequestBody TaskVo taskVo) {
        try{
            List<TaskVo> taskList = taskService.queryTasksBackend(taskVo);
            return R.success(taskList);
        }catch (Exception e){
            log.error(e.getMessage());
            return R.error("查询失败");
        }
    }

    @ApiOperation("统计个人任务数据")
    @GetMapping("/getOwnTask/{empId}")
    public R<OwnTaskVo> getOwnTask(@PathVariable Long empId) {
        return taskService.getOwnTask(empId);
    }

    @ApiOperation("获取个人任务列表")
    @GetMapping("/getMyTask/{orchardId}/{empId}")
    public R<List<TaskVo>> getMyTask(@PathVariable Long orchardId, @PathVariable Long empId) {
        return taskService.getMyTask(orchardId, empId);
    }

    @ApiOperation("获取指定类型任务列表")
    @GetMapping("/getTypeTask/{orchardId}/{type}/{empId}")
    public R<List<TaskVo>> getTypeTask(@PathVariable Long orchardId, @PathVariable String type, @PathVariable Long empId) {
        return taskService.getTypeTask(orchardId, type, empId);
    }

    @ApiOperation("获取未完成的任务类型")
    @GetMapping("/getTaskType/{empId}")
    public R<List<Map<String, Integer>>> getTaskType(@PathVariable Long empId) {
        return taskService.getTaskType(empId);
    }

    @ApiOperation("查询待审核任务")
    @GetMapping("getReview/{orchardId}")
    public R<List<TaskVo>> getReview(@PathVariable Long orchardId) {
        return taskService.getReview(orchardId);
    }

    @ApiOperation("审核通过")
    @PostMapping("/reviewOk")
    public R<String> reviewOk(@RequestBody List<Long> taskIds) {
        return taskService.reviewOk(taskIds);
    }

    @ApiOperation("审核不通过")
    @PostMapping("/reviewFaile")
    public R<String> reviewFaile(@RequestBody List<Long> taskIds) {
        return taskService.reviewFaile(taskIds);
    }

    @ApiOperation("获取该任务的报告")
    @GetMapping("/getTaskReport/{taskId}")
    public R<List<TaskVo>> getTaskReport(@PathVariable Long taskId) {
        return  taskService.getTaskReport(taskId);
    }

    @ApiOperation("countTaskByType")
    @GetMapping("/countTaskByType")
    public R<Map<String, Double>> countTaskByType() {
        return taskService.countTaskByType();
    }
}
