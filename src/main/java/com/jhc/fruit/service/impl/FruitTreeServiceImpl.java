package com.jhc.fruit.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.jhc.fruit.common.QRCodeUtil;
import com.jhc.fruit.common.R;
import com.jhc.fruit.dao.*;
import com.jhc.fruit.entity.*;
import com.jhc.fruit.entity.vo.FruitTreeVo;
import com.jhc.fruit.entity.vo.HealthTreeVo;
import com.jhc.fruit.entity.vo.TaskVo;
import com.jhc.fruit.entity.vo.TreeTypeVo;
import com.jhc.fruit.service.AreaService;
import com.jhc.fruit.service.FileService;
import com.jhc.fruit.service.FruitTreeService;
import com.jhc.fruit.service.TaskService;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.*;
import java.util.*;
import java.util.stream.Collectors;

/**
 * @author ：lcy
 * @description：TODO
 * @date ：2022/5/17 0017 9:42
 */
@Slf4j
@Service
public class FruitTreeServiceImpl extends ServiceImpl<FruitTreeDao, FruitTree> implements FruitTreeService {

    @Autowired
    private FruitTreeDao fruitTreeDao;
    @Autowired
    private FileService fileService;
    @Autowired
    private FertWaterDao fertWaterDao;
    @Autowired
    private AreaService areaService;
    @Autowired
    private TaskService taskService;
    @Autowired
    private AreaDao areaDao;
    @Autowired
    private TaskDao taskDao;

    @Override
    public Page<FruitTreeVo> page(Page<FruitTreeVo> page) {
        Page<FruitTreeVo> pageInfo = fruitTreeDao.page(page);
        return pageInfo;
    }

    //解析二维码，返回果树信息
    @Override
    public R<FruitTreeVo> showTree(MultipartFile imgFile) {
        //上传二维码
        String url = fileService.save(imgFile, "showTree");
        //解析二维码
        InputStream inputStream = fileService.getMinioFileInputStream(url);
        //删除二维码
        fileService.remove(url);
        //查找果树信息
        R<FruitTreeVo> result = treeInfo(QRCodeUtil.readTreeQRCode(inputStream));
        return result;
    }

    @Override
    public R<List<FruitTreeVo>> myTree(Long id) {
        //1. 判断id是否存在
        if (fruitTreeDao.selectById(id) == null) {
            return R.error("用户不存在");
        }else {
            //2.如果id存在，查询id对应用户负责的果树列表
            List<FruitTree> fruitTreeList = fruitTreeDao.myTree(id);
            //转换为vo类
            List<FruitTreeVo> fruitTreeVoList = new ArrayList<>();
            fruitTreeList.forEach(fruitTree -> {
                fruitTreeVoList.add(fruitToVo(fruitTree));
            });
            return R.success(fruitTreeVoList);
        }
        //3.返回列表
    }

    @Override
    public R<FruitTreeVo> myTreeInfo(Long userId, Long treeId) {
        FruitTree fruitTree = fruitTreeDao.selectById(treeId);

        if (fruitTree == null) {
            return R.error("果树不存在");
        }
        if (fruitTreeDao.selectById(userId) == null){
            return R.error("用户不存在");
        }
        //检查果树id是否属于该用户
        if (! fruitTreeDao.checkEmpOfTree(userId, treeId)){
            return R.error("非该果树负责人，无权限查看");
        }

        FruitTreeVo fruitTreeVo = fruitToVo(fruitTree);
        return R.success(fruitTreeVo);
    }

    @Override
    public List<FruitTree> getByIds(List<Long> ids) {
        LambdaQueryWrapper<FruitTree> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.in(FruitTree::getId, ids);
        List<FruitTree> fruitTreeList = fruitTreeDao.selectList(queryWrapper);
        return fruitTreeList;
    }

    @Override
    public boolean updateById(FruitTree fruitTree) {
        int i = fruitTreeDao.updateById(fruitTree);
        if(i == 1){
            return true;
        }
        return false;
    }

    @Override
    public FruitTree selectBy2Id(Long id) {
        FruitTree fruitTree = fruitTreeDao.selectBy2Id(id);
        return fruitTree;
    }

    @Override
    public List<FruitTreeVo> pageSelect(Page<FruitTreeVo> pageInfo,Long areaId) {
        List<FruitTree> page = fruitTreeDao.pageSelect(pageInfo,areaId);
        List<FruitTreeVo> fruitTreeVoList = new ArrayList<>();
        page.forEach(fruitTree -> {
            fruitTreeVoList.add(fruitToVo(fruitTree));
        });

        return fruitTreeVoList;
    }

    @Override
    public Long getTypeId(Long areaId) {
        Long typeId = fruitTreeDao.getTypeId(areaId);
        return typeId;
    }

    @Override
    public List<FruitType> showType() {
        List<FruitType> typeList = fruitTreeDao.showType();
        return typeList;
    }

    @Override
    public FruitTree getById(Long id) {
        FruitTree fruitTree = fruitTreeDao.getById(id);
        return fruitTree;
    }

    @Override
    public List<FruitTree> selectByAreaIdList(Long areaId) {
        List<FruitTree> fruitTreeList = fruitTreeDao.selectByAreaIdList(areaId);
        return fruitTreeList;
    }

    @Override
    public List<FruitTreeVo> sortByRipeCount(Long areaId) {
        List<FruitTreeVo> fruitTrees = fruitTreeDao.sortByRipeCount(areaId);
        return fruitTrees;
    }

    @Override
    public List<FruitTreeVo> sortByCount(Long areaId) {
        List<FruitTreeVo> fruitTrees = fruitTreeDao.sortByCount(areaId);
        return fruitTrees;
    }

    public FruitTreeVo fruitToVo(FruitTree fruitTree){
        FruitTreeVo fruitTreeVo = new FruitTreeVo();
        BeanUtils.copyProperties(fruitTree,fruitTreeVo);
        fruitTreeVo.setTypeId(fruitTree.getType());
        fruitTreeVo.setType(fruitTreeDao.selectByType(fruitTree.getType()));
        fruitTreeVo.setAreaName(fruitTreeDao.selectByAreaId(fruitTree.getAreaId()));
        fruitTreeVo.setAreaManagerName(fruitTreeDao.getAreaManagerName(fruitTree.getAreaId()));
        if (fruitTree.getImg() != null){
            fruitTreeVo.setImg(fileService.getImageUrl(fruitTree.getImg()));
        }else {
            fruitTreeVo.setImg(fileService.getImageUrl("headImg/default.png"));
        }
        if(fruitTreeDao.selectQR(fruitTree.getId()) != null){

            String url = fileService.getImageUrl(fruitTreeDao.selectQR(fruitTree.getId()));
            if(fileService.isPresignedUrlExpired(url)){
                fruitTreeVo.setUrl("URLExpired");
            }else {
                fruitTreeVo.setUrl(url);
            }

        }
        return fruitTreeVo;
    }

    @Override
    public void removeByAreaId(Long id) {
        fruitTreeDao.deleteByAreaId(id);
    }

    @Override
    public R<List<TreeTypeVo.TreeType>> statisticsTreeType(Long orchardId) {
        List<TreeTypeVo.TreeType> ttList = new ArrayList<>();
        //统计品种
        List<Long> typeIdsList = fruitTreeDao.selectTypeDISTINCT(orchardId);
        typeIdsList.forEach(typeId ->{
            Integer count = fruitTreeDao.countType(orchardId, typeId);
            String typeName = fruitTreeDao.selectByType(typeId);
            TreeTypeVo.TreeType treeType = new TreeTypeVo.TreeType(typeName, count);
            ttList.add(treeType);
        });
        return R.success(ttList);
    }

    @Override
    public R<List<TreeTypeVo>> statisticsTreeTypeArea(Long orchardId) {
        List<TreeTypeVo> result = new ArrayList<>();
        LambdaQueryWrapper<Area> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(Area::getOrchardId, orchardId);
        List<Area> areaList = areaDao.selectList(queryWrapper);
        for(Area area : areaList){
            TreeTypeVo treeTypeVo = new TreeTypeVo();
            List<TreeTypeVo.TreeType> ttList = new ArrayList<>();
            //统计品种
            List<Long> typeIdsList = fruitTreeDao.selectTypeDISTINCT(orchardId);
            typeIdsList.forEach(typeId ->{

                Integer count = fruitTreeDao.countType(orchardId, typeId);
                String typeName = fruitTreeDao.selectByType(typeId);
                TreeTypeVo.TreeType treeType = new TreeTypeVo.TreeType(typeName, count);
                ttList.add(treeType);
            });
            treeTypeVo.setArea(area);
            treeTypeVo.setTypeCount(ttList);
            result.add(treeTypeVo);
        }
        return R.success(result);
    }

    @Override
    public R<List<FruitTreeVo>> getByTask(Long orchardId, String type, Long empId) {
        //查询个人未完成的任务
        List<Task> tasks = taskDao.getMyTask(orchardId, empId);
        //提取相关类型
        List<TaskVo> taskVoList = new ArrayList<>();
        for (Task task : tasks) {
            if(task.getTaskType() != null && task.getTaskType().equals(type)){
                taskVoList.add(taskService.taskToVo(task));
            }
        }
        if(taskVoList.isEmpty()){
            return R.error("no task found");
        }else {
            List<FruitTreeVo> fruitTreeVoList = new ArrayList<>();
            for (TaskVo taskVo : taskVoList) {
                FruitTree fruitTree = fruitTreeDao.getTask(taskVo.getTreeId());
                if(fruitTree == null){
                    continue;
                }
                FruitTreeVo fruitTreeVo = fruitToVo(fruitTree);
                fruitTreeVo.setTasks(List.of(taskVo));
                fruitTreeVoList.add(fruitTreeVo);
            }
            return R.success(fruitTreeVoList);
        }

/*        //找出相关果树
        List<FruitTree> fruitTreeList = fruitTreeDao.selectByTaskType(orchardId, type, empId);
        List<FruitTreeVo> fruitTreeVoList = new ArrayList<>();
        for (FruitTree fruitTree : fruitTreeList) {
            FruitTreeVo fruitTreeVo = fruitToVo(fruitTree);
            //查询相关任务
            List<Task> taskList = fruitTreeDao.getByTask(fruitTree.getAreaId(), fruitTree.getId());
            log.info(taskList.toString());
            List<TaskVo> taskVoList = new ArrayList<>();
            taskList.forEach(item -> {
                TaskVo taskVo = taskService.taskToVo(item);
                taskVoList.add(taskVo);
            });
            //添加任务信息
            fruitTreeVo.setTasks(taskVoList);
            fruitTreeVoList.add(fruitTreeVo);
        }
        return R.success(fruitTreeVoList);*/
    }

    @Override
    public R<List<Map<String, Double>>> statisticsRipe(Long orchardId) {
        List<Map<String, Double>> result = new ArrayList<>();
        //统计品种
        List<Long> typeIdsList = fruitTreeDao.selectTypeDISTINCT(orchardId);
        typeIdsList.forEach(typeId ->{
            String typeName = fruitTreeDao.selectByType(typeId);
            Integer ripeCount = fruitTreeDao.getRipeSum(orchardId, typeId);
            Integer count = fruitTreeDao.getCountNumSum(orchardId, typeId);
            //非空处理
            int ripeCountInt = (ripeCount == null) ? 0 : ripeCount;
            int countInt = (count == null) ? 0 : count;
            // 计算熟果率
            Double ripeRate;
            if (countInt == 0) {
                // 总数为0时，熟果率设为0
                ripeRate = 0.0;
            } else {
                ripeRate = (double) ripeCountInt / countInt;
            }
            Map<String, Double> map = Map.of(typeName, ripeRate);
            result.add(map);
        });
        return R.success(result);
    }

    @Override
    public R<List<Map<String, Double>>> statisticsAreaRipe(Long orchardId) {
        //获取果园内所有区域
        List<Map<String, Double>> result = new ArrayList<>();
        LambdaQueryWrapper<Area> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(Area::getOrchardId, orchardId);
        List<Area> areaList = areaDao.selectList(queryWrapper);
        for(Area area : areaList){
            //统计区域内果树熟果率
            String areaName = areaDao.getAreaNameById(area.getId());
            Integer ripeCount = fruitTreeDao.getAreaRipeSum(orchardId, area.getId());
            Integer count = fruitTreeDao.getAreaCountNumSum(orchardId, area.getId());
            //非空处理
            int ripeCountInt = (ripeCount == null) ? 0 : ripeCount;
            int countInt = (count == null) ? 0 : count;
            // 计算熟果率
            Double ripeRate;
            if (countInt == 0) {
                // 总数为0时，熟果率设为0
                ripeRate = 0.0;
            } else {
                ripeRate = (double) ripeCountInt / countInt;
            }
            Map<String, Double> map = Map.of(areaName, ripeRate);
            result.add(map);
        }
        return R.success(result);
    }


    public FruitTree voToFruitTree(FruitTreeVo fruitTreeVo) {
        FruitTree fruitTree = new FruitTree();
        BeanUtils.copyProperties(fruitTreeVo, fruitTree);
        Long typeId = fruitTreeVo.getTypeId();
        fruitTree.setType(typeId);
        return fruitTree;
    }

    @Override
    public R<FruitTreeVo> treeInfo(Long treeId) {
        try{
            FruitTree fruitTree = fruitTreeDao.selectById(treeId);
            FruitTreeVo fruitTreeVo = fruitToVo(fruitTree);

            LambdaQueryWrapper<FertWater> queryWrapper = new LambdaQueryWrapper<>();
            queryWrapper.eq(FertWater::getTreeId, treeId);
            List<FertWater> fertWaters = fertWaterDao.selectList(queryWrapper);

            fruitTreeVo.setLogs(fertWaters);
            fruitTreeVo.setAreaName(areaService.getAreaNameById(fruitTree.getAreaId()));
            fruitTreeVo.setAreaManagerName(fruitTreeDao.getAreaManagerName(fruitTree.getAreaId()));
            fruitTreeVo.setOrchardName(fruitTreeDao.getOrchardNameById(fruitTree.getOrchardId()));
            return R.success(fruitTreeVo);
        }catch (Exception e){
            e.printStackTrace();
            return R.error("数据查询失败");
        }


    }
    @Override
    public R<FruitTreeVo> treeInfoSevenDays(Long treeId) {
        try{
            //通过treeId查询areaId
            Long areaId = this.getById(treeId).getAreaId();
            //通过areaId查询对应区域的task
            List<TaskVo> taskVos = taskService.getByAreaId(areaId);
            FruitTree fruitTree = fruitTreeDao.selectById(treeId);
            FruitTreeVo fruitTreeVo = fruitToVo(fruitTree);

            LambdaQueryWrapper<FertWater> queryWrapper = new LambdaQueryWrapper<>();
            queryWrapper.eq(FertWater::getTreeId, treeId);
            //查询最近7天的记录
            queryWrapper.ge(FertWater::getCreateTime, System.currentTimeMillis() - 7L * 24 * 60 * 60 * 1000);
            List<FertWater> fertWaters = fertWaterDao.selectList(queryWrapper);

            fruitTreeVo.setLogs(fertWaters);
            fruitTreeVo.setAreaName(areaService.getAreaNameById(fruitTree.getAreaId()));
            fruitTreeVo.setAreaManagerName(fruitTreeDao.getAreaManagerName(fruitTree.getAreaId()));
            fruitTreeVo.setOrchardName(fruitTreeDao.getOrchardNameById(fruitTree.getOrchardId()));
            fruitTreeVo.setTasks(taskVos);
            return R.success(fruitTreeVo);
        }catch (Exception e){
            e.printStackTrace();
            return R.error("数据查询失败");
        }


    }
    public R<List<String>> createTreeQRList(List<Long> ids) {
        //先判断该id对应的果树是否存在
        List<FruitTree> fruitTrees = getByIds(ids);
        if (fruitTrees == null || fruitTrees.isEmpty()) {
            return  R.error("目标果树不存在");
        }

        //校验「传入的ID数量」和「查询到的数量」是否一致（避免部分ID不存在）
        if (fruitTrees.size() != ids.size()) {
            //找出不存在的ID
            Set<Long> existIds = fruitTrees.stream()
                    .map(FruitTree::getId)
                    .collect(Collectors.toSet());
            Set<Long> notExistIds = ids.stream()
                    .filter(id -> !existIds.contains(id))
                    .collect(Collectors.toSet());
            return R.error("部分果树不存在，不存在的ID：" + notExistIds);
        }
        //如果果树都存在
        List<String> QRUrls = new ArrayList<>();
        for (FruitTree fruitTree : fruitTrees) {
            Long id = fruitTree.getId();
            String url = fileService.save(
                    QRCodeUtil.createQROfString(String.valueOf(id)), "QRtree");
            String imageUrl = fileService.getImageUrl(url);
            QRUrls.add(imageUrl);
            fruitTreeDao.saveQRUrl(id, url);
        }

        return R.success(QRUrls);
    }

    @Override
    public R<List<String>> selectQR(List<Long> ids) {
        List<String> urls = new ArrayList<>();
        if (ids == null || ids.isEmpty()) {
            return R.error("果树列表为空");
        }
        for (Long id : ids) {
            String ulr = fruitTreeDao.selectQR(id);
            urls.add(ulr);
        }
        return R.success(urls);
    }

    public Long selectOrchardIdByTreeId(Long treeId){
        return fruitTreeDao.selectOrchardIdByTreeId(treeId);
    }

    @Override
    public R<HealthTreeVo> countIllness() {
        HealthTreeVo healthTreeVo = new HealthTreeVo();
        Integer disasterTree = fruitTreeDao.countDisasterTree();
        healthTreeVo.setDisasterTree(disasterTree);
        Integer total = fruitTreeDao.getTotal();
        Integer healthTree = total - disasterTree;
        healthTreeVo.setHealthTree(healthTree);
        healthTreeVo.setHealth((double) (healthTree/total));
        healthTreeVo.setDisaster((double) (disasterTree/total));
        return R.success(healthTreeVo);
    }


}
