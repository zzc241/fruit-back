package com.jhc.fruit.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.jhc.fruit.common.R;
import com.jhc.fruit.dao.*;
import com.jhc.fruit.entity.*;
import com.jhc.fruit.entity.vo.*;
import com.jhc.fruit.service.*;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


import java.math.BigDecimal;
import java.math.RoundingMode;
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
    @Autowired
    private ReportService reportService;
    @Autowired
    private FruitTreeDao fruitTreeDao;
    @Autowired
    private FileService fileService;

    /*新增任务*/
    @Override
    public Long addTask(TaskVo taskVo) {
        try {
            Task task = voToTask(taskVo);
            Long areaId = task.getAreaId();
            if (areaId != null && task.getTreeId() == null) {
                List<FruitTree> fruitTrees = fruitTreeDao.getTreeByArea(areaId);
                //设置创建时间
                task.setCreateTime(LocalDateTime.now());
                task.setUpdateTime(LocalDateTime.now());
                Long reportId = null;
                if(taskVo.getReportVo() != null){
                    reportId = taskVo.getReportVo().getId();
                }
                for (FruitTree fruitTree : fruitTrees) {
                    task.setTreeId(fruitTree.getId());
                    if(reportId != null){
                        task.setReportId(reportId);
                    }
                    taskDao.insert(task);
                }
            }

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
            if(taskVo.getReceiverId() != null){
                task.setReceiverId(taskVo.getReceiverId());
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
        if (taskVo.getReceiverId() != null) {
            task.setReportId(taskVo.getReportVo().getId());
        }
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
        String priorityName;
        if (task.getPriority() == null || task.getPriority() == 0) {
            priorityName = "日常任务";
        } else {
            priorityName = "紧急任务";
        }
        taskVo.setPriority(priorityName);
        taskVo.setOrchardName(taskDao.getOrchardName(task.getOrchardId()));
        if(task.getReceiverId() != null){
            taskVo.setReceiverName(taskDao.getReceiverName(task.getReceiverId()));
        }else {
            taskVo.setReceiverName(taskDao.selectRecName(task));
        }

        taskVo.setUsername(
                employeeService.getEmpNameById(
                        task.getCreatorId()));
        taskVo.setAreaName(areaDao.getAreaNameById(task.getAreaId()));
        if (! ((taskImgService.getImg(task.getId()) == null))){
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
//        if (taskVo.getGroupId() != null) {
//            queryWrapper.eq(Task::getGroupId, taskVo.getGroupId());
//        }
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
//        Long orchardId = employeeDao.getOrchardId(empId);
//        //获取个人的所有任务
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


    @Override
    public R<List<TaskVo>> getReview(Long orchardId) {
        //查询待审核的任务
        //查询该果园待审核的任务
        List<Task> tasks = taskDao.getReview(orchardId);
        List<TaskVo> taskVos = new ArrayList<>();
        for (Task task : tasks) {
            TaskVo taskVo = taskToVo(task);
            //填充任务报告
            Report report = taskDao.getReviewReport(task.getReportId());
            ReportVo reportVo = reportService.selectReport(List.of(report.getId())).getData().get(0);
            taskVo.setReportVo(reportVo);
            taskVos.add(taskVo);
        }
        return R.success(taskVos);
    }

    @Override
    public R<String> reviewOk(List<Long> taskIds) {
        List<Task> tasks = new ArrayList<>();
        for (Long taskId : taskIds) {
            Task task = taskDao.selectById(taskId);
            task.setStatus(3);
            taskDao.updateById(task);
        }
        return R.success();
    }

    @Override
    public R<String> reviewFaile(List<Long> taskIds) {
        List<Task> tasks = new ArrayList<>();
        for (Long taskId : taskIds) {
            Task task = taskDao.selectById(taskId);
            task.setStatus(5);
            taskDao.updateById(task);
        }
        return R.success();
    }

    /**
     * 根据任务类型统计完成率
     * @return 封装好的结果，key=任务类型，value=该类型任务完成率（0~1之间）
     */
    @Override
    public R<Map<String, Double>> countTaskByType() {
        // 1. 获取数据库中所有的任务类型列表
        List<String> typeList = taskDao.getAllType();

        // 2. 遍历任务类型，统计每种类型的任务总数量
        Map<String, Integer> totalCountMap = new HashMap<>();
        for (String type : typeList) {
            // 根据任务类型查询总数量
            Integer totalCount = taskDao.countByType(type);
            totalCountMap.put(type, totalCount);
        }

        // 3. 遍历任务类型，统计每种类型已完成的任务数量，并计算完成率
        Map<String, Double> completionRateMap = new HashMap<>();
        for (Map.Entry<String, Integer> entry : totalCountMap.entrySet()) {
            String taskType = entry.getKey();
            // 当前类型任务总数
            Integer total = entry.getValue();
            // 当前类型已完成任务数
            Integer completedCount = taskDao.countCompletedByType(taskType);

            // 计算完成率：已完成数量 / 总数量，转为double避免整数除法
            double rate = completedCount * 1.0 / total;
            completionRateMap.put(taskType, rate);
        }

        // 打印任务完成率结果日志
        log.info("各类型任务完成率统计结果：{}", completionRateMap.toString());

        // 封装并返回成功结果
        return R.success(completionRateMap);
    }

    @Override
    public R<TaskStatisticsVO> countByStatus() {
        //统计待完成的任务、待发布的任务、已完成的任务、逾期任务
        TaskStatisticsVO taskStatisticsVO = new TaskStatisticsVO();
        //统计已接收但是还没有完成的任务,待执行任务
        Integer unCompletedTasks = taskDao.getByStatus(1L);
        //统计已完成任务
        Integer completedTasks = taskDao.getByStatus(3L);
        //统计逾期任务
        Integer overdueCount = taskDao.getOverDue();
        //未完成任务占比(未完成 / 未完成+已完成)
        Double pendingExecuteRemainingRate = unCompletedTasks / (double) (unCompletedTasks + completedTasks);
        Double completionRate = 1 - pendingExecuteRemainingRate;
        //待发布任务
        Integer pendingDispatchCount = taskDao.getByStatus(6L);
        //存入属性
        taskStatisticsVO.setPendingDispatchCount(pendingDispatchCount);
        taskStatisticsVO.setCompletionRate(roundDouble(completionRate * 100, 1));
        taskStatisticsVO.setOverdueCount(overdueCount);
        taskStatisticsVO.setPendingExecuteRemainingRate(roundDouble(pendingExecuteRemainingRate * 100, 1));
        taskStatisticsVO.setPendingExecuteCount(unCompletedTasks);
        taskStatisticsVO.setCompletedCount(completedTasks);

        return R.success(taskStatisticsVO);
    }

    public static Double roundDouble(Double value, int scale) {
        if (value == null) {
            return 0.0;
        }
        return new BigDecimal(value)
                .setScale(scale, RoundingMode.HALF_UP) // 四舍五入
                .doubleValue();
    }
}
