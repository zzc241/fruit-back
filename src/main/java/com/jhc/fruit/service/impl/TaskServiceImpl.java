package com.jhc.fruit.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.jhc.fruit.common.R;
import com.jhc.fruit.dao.AreaDao;
import com.jhc.fruit.dao.EmployeeDao;
import com.jhc.fruit.dao.TaskDao;
import com.jhc.fruit.entity.*;
import com.jhc.fruit.entity.vo.OwnTaskVo;
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
import java.util.*;
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
    @Autowired
    private AreaDao areaDao;
    @Autowired
    private EmployeeDao employeeDao;

    /*新增任务*/
    @Override
    public Long addTask(TaskVo taskVo) {
        try {
            Task task = voToTask(taskVo);
            //设置了区域范围任务
            if (task.getTreeId() == null && task.getAreaId() != null) {
                List<FruitTree> treeList = taskDao.getTreeByAreaId(task.getAreaId());
                for (FruitTree fruitTree : treeList) {
                    Task taskArea = new Task();
                    BeanUtils.copyProperties(task, taskArea);
                    taskArea.setTreeId(fruitTree.getId());
                    this.taskDao.insert(taskArea);
                    //保存任务图片
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
                }
            }else {
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
            Iterator<String> iterator = null;
            Long id = task.getId();
            taskImgService.removeByTaskId(id);
            if(!(img == null || img.size() == 0)){
                iterator = img.iterator();
                while (iterator.hasNext()) {
                    String imgUrl = iterator.next();
                    if (img != null && img.size() > 0) {
                        TaskImg taskImg = new TaskImg();
                        taskImg.setImg(imgUrl);
                        taskImg.setTaskId(task.getId());
                        taskImgService.addTaskImg(taskImg);
                    }
                }
            }
        }catch (Exception e){
            log.error(e.getMessage());
            throw new RuntimeException(e);
        }
    }

        /*根据id查询*/
    @Override
    public List<TaskVo> getByOrchardId(Long OrchardId) {
        List<Task> tasks = taskDao.selectByOrchardId(OrchardId);
        List<TaskVo> taskVos = tasks.stream()
                .map(this::taskToVo)
                .collect(Collectors.toList());
        for (TaskVo taskVo : taskVos) {
            Task task = taskDao.selectById(taskVo.getId());
            taskVo.setUsername(employeeService.getById(task.getCreatorId()).getUsername());
            taskVo.setOrchardName(orchardService.getOrchardNameById(task.getOrchardId()));
            taskVo.setImgsURL(taskImgService.getImg(task.getId()));
        }
        
        return taskVos;
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
        List<Task> tasks = empGroupService.getTasks(taskVo.getReceiverId());
        // if (tasks == null || tasks.isEmpty()) {
        //     return new ArrayList<>();
        // }
        
        Set<Long> taskIdSet = new HashSet<>();
        List<Task> allTasks = new ArrayList<>();
        
        // 查询1：基于 taskIds
        List<Long> taskIds = tasks.stream().map(Task::getId).collect(Collectors.toList());
        if (!taskIds.isEmpty()) {  // 关键：只有非空才添加 IN 条件
            LambdaQueryWrapper<Task> queryWrapper = new LambdaQueryWrapper<>();
            queryWrapper.in(Task::getId, taskIds);
            if (taskVo.getStatus() != null) {
                queryWrapper.eq(Task::getStatus, taskVo.getStatus());
            }
            addTasksWithoutDuplicate(allTasks, taskIdSet, taskDao.selectList(queryWrapper));
        }
        // 查询2：基于 areaId
        List<Long> areaIds = areaDao.selectByEmpId(taskVo.getReceiverId());
        if (!areaIds.isEmpty()) {
            LambdaQueryWrapper<Task> queryWrapperArea = new LambdaQueryWrapper<>();
            queryWrapperArea.in(Task::getAreaId, areaIds);  // 用 in，不是 eq
            if (taskVo.getStatus() != null) {
                queryWrapperArea.eq(Task::getStatus, taskVo.getStatus());
            }
            addTasksWithoutDuplicate(allTasks, taskIdSet, taskDao.selectList(queryWrapperArea));
        }
        
        // 查询3：基于 orchardId
        List<Long> orchardIds = areaDao.getOrchardIdByEmpId(taskVo.getReceiverId());
        if (orchardIds != null && !orchardIds.isEmpty()) {
            LambdaQueryWrapper<Task> queryWrapperOrchard = new LambdaQueryWrapper<>();
            queryWrapperOrchard.in(Task::getOrchardId, orchardIds);  // 用 in
            if (taskVo.getStatus() != null) {
                queryWrapperOrchard.eq(Task::getStatus, taskVo.getStatus());
            }
            addTasksWithoutDuplicate(allTasks, taskIdSet, taskDao.selectList(queryWrapperOrchard));
        }
        // 转换为 Vo 并设置图片
        List<TaskVo> taskVos = allTasks.stream()
                .map(this::taskToVo)
                .collect(Collectors.toList());
        
        taskVos.forEach(taskVoCurr -> 
            taskVoCurr.setImgsURL(taskImgService.getImg(taskVoCurr.getId()))
        );
        
        return taskVos;
    }

    // 辅助方法：添加任务到列表，避免重复
    private void addTasksWithoutDuplicate(List<Task> allTasks, Set<Long> taskIdSet, List<Task> newTasks) {
        for (Task task : newTasks) {
            if (!taskIdSet.contains(task.getId())) {
                taskIdSet.add(task.getId());
                allTasks.add(task);
            }
        }
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

    @Override
    public TaskVo taskToVo(Task task) {
        TaskVo taskVo = new TaskVo();
        BeanUtils.copyProperties(task, taskVo);
        taskVo.setUsername(
                employeeService.getEmpNameById(
                        task.getCreatorId()));
        taskVo.setAreaName(areaDao.getAreaNameById(task.getAreaId()));
        if (!(taskImgService.getImg(task.getId()) == null)){
            taskVo.setImgsURL(
                    taskImgService.getImg(task.getId())
            );
        }

        return taskVo;
    }

    public List<TaskVo> queryTasksBackend(TaskVo taskVo){
        LambdaQueryWrapper<Task> queryWrapperTask = new LambdaQueryWrapper<>();
        queryWrapperTask.eq(Task::getCreatorId, taskVo.getCreatorId());
        List<Task> tasks = this.list(queryWrapperTask);
        if (tasks == null || tasks.isEmpty()) {
            return new ArrayList<>();
        }
        List<Long> taskIds = tasks.stream()
            .map(Task::getId)
            .collect(Collectors.toList());
        LambdaQueryWrapper<Task> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.in(Task::getId, taskIds);
        if (taskVo.getOrchardId() != null) {
            queryWrapper.eq(Task::getOrchardId, taskVo.getOrchardId());
        }
        if (taskVo.getGroupId() != null) {
            queryWrapper.eq(Task::getGroupId, taskVo.getGroupId());
        }
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

    public List<TaskVo> getByAreaId(Long areaId) { 
        LambdaQueryWrapper<Task> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(Task::getAreaId, areaId);
        List<Task> tasks = this.list(queryWrapper);
        List<TaskVo> taskVos = tasks.stream()
                .map(this::taskToVo)
                .collect(Collectors.toList());
        return taskVos;
    }

    @Override
    public R<OwnTaskVo> getOwnTask(Long empId) {
        OwnTaskVo ownTaskVo = new OwnTaskVo();
        //先通过员工id查找关联任务(员工id-》员工组-》任务)
        List<Task> taskList = taskDao.getOwnTask(empId);
        if (taskList == null || taskList.isEmpty()) {
            return R.error("没有相关任务");
        }
        //开始统计数据
        ownTaskVo.setTasksCount(taskList.size());
        //类型统计
        List<Map<String, Integer>> typeCount = new ArrayList<>();
        List<String> typeList = taskDao.getType(empId);
        for (String type : typeList) {
            Map<String, Integer> map = new HashMap<>();
            Integer count = taskDao.countType(empId, type);
            map.put(type, count);
            typeCount.add(map);
        }
        ownTaskVo.setTypeCount(typeCount);
        //统计肥料
        List<Map<String, Double>> fertCount = new ArrayList<>();
        List<String> fertTypeList = taskDao.getFertType(empId);
        for(String fertType : fertTypeList){
            Map<String, Double> map = new HashMap<>();
            Double countNum = taskDao.getfertUseCount(empId, fertType);
            map.put(fertType, countNum);
            fertCount.add(map);
        }
        ownTaskVo.setFertCount(fertCount);
        //统计农药
        List<Map<String, Double>> pesticideCount = new ArrayList<>();
        List<String> pesticideTypeList = taskDao.getpesticideType(empId);
        for(String pesticideType : pesticideTypeList){
            Map<String, Double> map = new HashMap<>();
            Double countNum = taskDao.getPesticideUseCount(empId, pesticideType);
            map.put(pesticideType, countNum);
            pesticideCount.add(map);
        }
        ownTaskVo.setFertCount(fertCount);
        return R.success(ownTaskVo);
    }

    @Override
    public R<List<TaskVo>> getMyTask(Long orchardId, Long empId) {
        List<Task> tasks = new ArrayList<>();
        tasks = taskDao.getMyTask(orchardId, empId);
        List<TaskVo> taskVos = new ArrayList<>();
        for (Task task : tasks) {
            taskVos.add(taskToVo(task));
        }
        return R.success(taskVos);
    }

    @Override
    public R<List<TaskVo>> getTypeTask(Long orchardId, String type, Long empId) {
        List<Task> tasks = taskDao.getTaskByType(orchardId, type, empId);
        List<TaskVo> taskVos = new ArrayList<>();
        for (Task task : tasks) {
            taskVos.add(taskToVo(task));
        }
        return R.success(taskVos);
    }

    @Override
    public R<List<Map<String, Integer>>> getTaskType(Long empId) {
        Employee employee = employeeDao.selectById(empId);
        //获取个人的所有任务
        R<List<TaskVo>> taskVos = getMyTask(employee.getOrchardId(), empId);
        List<TaskVo> taskVoList = taskVos.getData();
        //统计类型以及未完成的数量
        Map<String, Integer> map = new HashMap<>();
        for (TaskVo taskVo : taskVoList) {
            String type = taskVo.getTaskType();
            map.put(type, map.getOrDefault(type, 0) + 1);
        }
        List<Map<String, Integer>> resultList = new ArrayList<>();
        map.forEach((key, value) -> resultList.add((Map.of(key, value))));
        return R.success(resultList);
    }
}
