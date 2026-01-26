package com.jhc.fruit.controller;

import com.jhc.fruit.common.R;
import com.jhc.fruit.entity.Task;
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
@Api(tags = "后台：任务管理接口")
public class TaskController {

    @Autowired
    private TaskService taskService;

    /*新增任务*/
    @ApiOperation(value = "新增任务")
    @PostMapping("/add")
    public R<String> addTask(@RequestBody Task task) {
        taskService.addTask(task);
        return R.success("新增成功");
    }

    @ApiOperation(value = "修改任务")
    @PostMapping("update")
    public R<String> updateTask(@RequestBody Task task) {
        taskService.updateTask(task);
        return R.success("修改成功");
    }

    @ApiOperation(value = "根据id删除任务")
    @DeleteMapping("/delete/{id}")
    public R<String> deleteTask(@PathVariable Long id) {
        taskService.removeById(id);
        return R.success("删除成功");
    }

    @ApiOperation(value = "批量删除任务")
    @DeleteMapping("/delete")
    public R<String> deleteTask(@RequestBody List<Long> taskIds) {
        taskService.removeByList(taskIds);
        return R.success("删除成功");
    }

    @ApiOperation(value = "今日发布的任务")
    @GetMapping("/todayTask")
    public R<List<Task>> todayTask() {
        LocalDateTime now = LocalDateTime.now();
        // 今日开始时间：年-月-日 00:00:00
        LocalDateTime start = now.withHour(0).withMinute(0).withSecond(0);
        // 今日结束时间：年-月-日 23:59:59
        LocalDateTime end = now.withHour(23).withMinute(59).withSecond(59);
        List<Task> tasks = taskService.selectByPostTime(start, end);
        return R.success(tasks);
    }
}
