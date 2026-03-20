package com.jhc.fruit.service.impl;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.jhc.fruit.common.BaseContext;
import com.jhc.fruit.dao.AnnounceInfoDao;
import com.jhc.fruit.entity.AnnounceInfo;
import com.jhc.fruit.entity.Employee;
import com.jhc.fruit.entity.vo.AnnounceInfoVo;
import com.jhc.fruit.service.EmpGroupService;
import com.jhc.fruit.service.EmployeeService;
import com.jhc.fruit.service.AnnounceInfoService;

import com.jhc.fruit.service.OrchardService;

import cn.hutool.core.util.StrUtil;
import lombok.extern.slf4j.Slf4j;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Random;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * @author ：lcy
 * @description：TODO
 * @date ：2022/5/30 0030 10:07
 * 
 */
/**
 * @author ：zzc
 * @description：再次声明getNoFinish和getFinish方法只是用于测试
 * @date ：2026/1/23
 * @description：去掉测试方法
 * @date ：2026/1/29
 */
@Slf4j
@Service
public class AnnounceInfoServiceImpl extends ServiceImpl<AnnounceInfoDao, AnnounceInfo> implements AnnounceInfoService {
    @Autowired
    private AnnounceInfoDao announceInfoDao;
    @Autowired
    private EmployeeService employeeService;
    @Autowired
    private EmpGroupService empGroupService;
    @Autowired
    private OrchardService orchardService;
    @Override
    public Page<AnnounceInfoVo> getPageInfo(AnnounceInfoVo pickInfoVo) { 
        Page<AnnounceInfo> pageInfo = new Page<>(pickInfoVo.getPage(), 1000);
        LambdaQueryWrapper<AnnounceInfo> queryWrapper = new LambdaQueryWrapper<>();

        if (StrUtil.isNotBlank(pickInfoVo.getTitle())) {
            queryWrapper.like(AnnounceInfo::getTitle, pickInfoVo.getTitle());
        }

        if (pickInfoVo.getEmpId() != null) {
            queryWrapper.eq(AnnounceInfo::getEmpId, pickInfoVo.getEmpId());
        }
        if(pickInfoVo.getSenderId() != null){
            queryWrapper.eq(AnnounceInfo::getSenderId, pickInfoVo.getSenderId());
        }
        if(pickInfoVo.getOrchardId() != null){
            queryWrapper.eq(AnnounceInfo::getOrchardId, pickInfoVo.getOrchardId());
        }

        if (pickInfoVo.getIsFinish() != null) {
            Long isFinishInt = pickInfoVo.getIsFinish();
            queryWrapper.eq(AnnounceInfo::getIsFinish, isFinishInt);
        }
        if(pickInfoVo.getIsRead() != null){
            Long isReadInt = pickInfoVo.getIsRead();
            queryWrapper.eq(AnnounceInfo::getIsRead, isReadInt);
        }
        // 按时间倒序排列
        queryWrapper.orderByDesc(AnnounceInfo::getCreateTime);
        pageInfo = page(pageInfo , queryWrapper);

        // 转换分页数据
        Page<AnnounceInfoVo> pageInfoVo = new Page<>(
            pageInfo.getCurrent(),
            pageInfo.getSize(),
            pageInfo.getTotal()
        );
        pageInfoVo.setPages(pageInfo.getPages());


        // 2. 批量收集ID（新增）
        List<AnnounceInfo> records = pageInfo.getRecords();
        Set<Long> empIds = new HashSet<>();
        Set<Long> groupIds = new HashSet<>();
        for (AnnounceInfo pickInfo : records) {
            if(pickInfo.getSenderId() != null) empIds.add(pickInfo.getSenderId());
            if(pickInfo.getEmpId() != null) empIds.add(pickInfo.getEmpId());
            if(pickInfo.getGroupId() != null) groupIds.add(pickInfo.getGroupId());
        }

        // 3. 批量查询名称（1次DB查询，非N次）
        Map<Long, String> empNameMapResult = employeeService.getEmpNameMapByIds(empIds);
        Map<Long, String> groupNameMapResult = empGroupService.getGroupNameMapByIds(groupIds);

        // 处理空值
        Map<Long, String> empNameMap = empNameMapResult == null ? new HashMap<>() : empNameMapResult;
        Map<Long, String> groupNameMap = groupNameMapResult == null ? new HashMap<>() : groupNameMapResult;
        // 转换records
        List<AnnounceInfoVo> voList = records.stream()
            .map(pickInfo -> {
                AnnounceInfoVo vo = new AnnounceInfoVo();
                BeanUtils.copyProperties(pickInfo, vo);

                // 设置关联的名称信息
                vo.setSenderName(empNameMap.get(pickInfo.getSenderId()));
                vo.setEmpName(empNameMap.get(pickInfo.getEmpId()));
                if(pickInfo.getGroupId() != null){
                    vo.setGroupName(groupNameMap.get(pickInfo.getGroupId()));
                }
                
                return vo;
            })
            .collect(Collectors.toList());
        pageInfoVo.setRecords(voList);
        return pageInfoVo;
    }
    @Override
    public List<AnnounceInfoVo> getInfo(AnnounceInfoVo pickInfoVo) {
        // 构建查询条件
        LambdaQueryWrapper<AnnounceInfo> queryWrapper = new LambdaQueryWrapper<>();
        
        if (StrUtil.isNotBlank(pickInfoVo.getTitle())) {
            queryWrapper.like(AnnounceInfo::getTitle, pickInfoVo.getTitle());
        }

        if (pickInfoVo.getEmpId() != null) {
            queryWrapper.eq(AnnounceInfo::getEmpId, pickInfoVo.getEmpId());
        }
        if(pickInfoVo.getOrchardId() != null){
            queryWrapper.eq(AnnounceInfo::getOrchardId, pickInfoVo.getOrchardId());
        }
        if(pickInfoVo.getIsFinish() != null){
            Long isFinishInt = pickInfoVo.getIsFinish();
            queryWrapper.eq(AnnounceInfo::getIsFinish, isFinishInt);
        }
        if(pickInfoVo.getIsRead() != null){
            Long isReadInt = pickInfoVo.getIsRead();
            queryWrapper.eq(AnnounceInfo::getIsRead, isReadInt);
        }
        
        // 按时间倒序排列
        queryWrapper.orderByDesc(AnnounceInfo::getCreateTime);

        // 限制最多返回500条，避免数据过大
        queryWrapper.last("LIMIT 500");
        List<AnnounceInfo> pickInfos = this.list(queryWrapper);

        // 2. 批量收集ID（新增）
        List<AnnounceInfo> records = pickInfos;
        Set<Long> empIds = new HashSet<>();
        Set<Long> groupIds = new HashSet<>();
        Set<Long> areaIds = new HashSet<>();  // 新增：收集区域ID
        for (AnnounceInfo pickInfo : records) {
            if(pickInfo.getSenderId() != null) empIds.add(pickInfo.getSenderId());
            if(pickInfo.getEmpId() != null) empIds.add(pickInfo.getEmpId());
            if(pickInfo.getGroupId() != null) groupIds.add(pickInfo.getGroupId());
            if(pickInfo.getAreaId() != null) areaIds.add(pickInfo.getAreaId());
        }

        // 3. 批量查询名称（1次DB查询，而非N次）
        Map<Long, String> groupNameMap = empGroupService.getGroupNameMapByIds(groupIds);
        Map<Long, String> empNameMap = Optional.ofNullable(employeeService.getEmpNameMapByIds(empIds))
        .orElse(new HashMap<>());

        return pickInfos.stream().map(pickInfo -> {
            AnnounceInfoVo pickInfoVoResult = new AnnounceInfoVo();
            BeanUtils.copyProperties(pickInfo, pickInfoVoResult);
            // 设置关联的名称信息
                pickInfoVoResult.setSenderName(empNameMap.get(pickInfo.getSenderId()));
                pickInfoVoResult.setEmpName(empNameMap.get(pickInfo.getEmpId()));
                // if(pickInfo.getGroupId() != null){
                //     pickInfoVoResult.setGroupName(groupNameMap.get(pickInfo.getGroupId()));
                // }
                if (pickInfo.getAreaId() != null) {
                    pickInfoVoResult.setAreaName(announceInfoDao.getAreaNameById(pickInfo.getAreaId() == null ? 0L : pickInfo.getAreaId()));
                }
            return pickInfoVoResult;
        }).collect(Collectors.toList());
    }

    public String changeInfo(AnnounceInfoVo pickInfoVo){
        LambdaQueryWrapper<AnnounceInfo> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(AnnounceInfo::getId, pickInfoVo.getId());
        AnnounceInfo pickInfo = this.getOne(queryWrapper);
        Long senderId = pickInfoVo.getSenderId();
        Long userId = BaseContext.getId();
        if(senderId != null && !senderId.equals(userId)){
            return "当前用户传来的不是登录用户，请检查登录状态";
        }
        try { 
            if(senderId != null){
                String senderName = employeeService.getEmpNameById(senderId);
                if(senderName == null){
                    return "未获取到当前发布者用户信息，请检查senderId是否正常";
                }
            }
        } catch (Exception e) {
            return "未获取到当前员工信息，请检查empId是否正常";
        }
        try {
            Long orchardId = pickInfoVo.getOrchardId();
            if(orchardId != null){
                String orchardName = orchardService.getOrchardNameById(orchardId);
                if(orchardName == null){
                    return "未获取到当前果园信息，请检查orchardId是否正常";
                }
            }
            
        } catch (Exception e) {
            return "未获取到当前果园信息，请检查orchardId是否正常";
        }
        // 将pickInfoVo中的属性复制到pickInfo中,但排除id，createTime，updateTime，createUser，updateUser这些不需要修改的字段
        BeanUtils.copyProperties(pickInfoVo, pickInfo , "id" , "createTime" , "updateTime" , "createUser" , "updateUser");
        return this.updateById(pickInfo) ? "修改成功" : "修改失败";
    }

    public String updateReadInfo(Long id , Long isRead){
        if(id == null){
            return "未获取到通知id，请检查id是否正常";
        }
        AnnounceInfo info = this.getById(id);
        if(info == null){
            return "通知不存在";
        }
        info.setIsRead(isRead);
        return this.updateById(info) ? "已读成功" : "已读失败";
    }
    public String updateFinishInfo(Long id , Long isFinish){
        if(id == null){
            return "未获取到通知id，请检查id是否正常";
        }
        AnnounceInfo info = this.getById(id);
        if(info == null){
            return "通知不存在";
        }
        info.setIsFinish(isFinish);
        return this.updateById(info) ? "完成成功" : "完成失败";
    }

    //TODO : 实现消息群发特定组功能
    public String saveDeployInfo(AnnounceInfoVo announceInfoVo){
        Random random = new Random();
        Long senderId = announceInfoVo.getSenderId();
        log.info("senderId:" + senderId);
        Long userId = BaseContext.getId();
        log.info("userId:" + userId);
        if(!senderId.equals(userId)){
            return "当前用户传来的不是登录用户，请检查登录状态";
        }
        //判断员工的is_admin是否为2，如果是2就将status改成1（已审核），如果是3就将status改为0（未审核）
        Employee employee = employeeService.getById(senderId);
        
        announceInfoVo.setStatus(0); // 未审核
        if (employee != null && employee.getIsAdmin() == 2) {
            announceInfoVo.setStatus(1); // 已审核
        }
        
        AnnounceInfo announceInfo = new AnnounceInfo();
        BeanUtils.copyProperties(announceInfoVo, announceInfo);
        announceInfo.setAddress("浙江省山东市甘肃镇内蒙古自治区");
        announceInfo.setDistance(random.nextDouble() * 100);
        this.save(announceInfo);
        return "发布成功";
    }
}
