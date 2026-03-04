package com.jhc.fruit.service.impl;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.jhc.fruit.common.BaseContext;
import com.jhc.fruit.common.R;
import com.jhc.fruit.dao.AnnounceInfoDao;
import com.jhc.fruit.entity.AnnounceInfo;
import com.jhc.fruit.entity.vo.AnnounceInfoVo;
import com.jhc.fruit.service.EmpGroupService;
import com.jhc.fruit.service.EmployeeService;
import com.jhc.fruit.service.AnnounceInfoService;

import lombok.extern.slf4j.Slf4j;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.List;
import java.util.Map;
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
    // @Autowired
    // private PickInfoDao pickInfoDao;
    @Autowired
    private EmployeeService employeeService;
    @Autowired
    private EmpGroupService empGroupService;
    @Autowired
    private OrchardServiceImpl orchardServiceImpl;
    @Override
    public Page<AnnounceInfoVo> getPageInfo(AnnounceInfoVo pickInfoVo) { 
        Page<AnnounceInfo> pageInfo = new Page<>(pickInfoVo.getPage(), pickInfoVo.getPageSize());
        LambdaQueryWrapper<AnnounceInfo> queryWrapper = new LambdaQueryWrapper<>();

        if (StringUtils.isNotBlank(pickInfoVo.getTitle())) {
            queryWrapper.like(AnnounceInfo::getTitle, pickInfoVo.getTitle());
        }

        if (pickInfoVo.getEmpId() != null) {
            queryWrapper.eq(AnnounceInfo::getEmpId, pickInfoVo.getEmpId());
        }
        if(pickInfoVo.getSenderId() != null){
            queryWrapper.eq(AnnounceInfo::getSenderId, pickInfoVo.getSenderId());
        }

        if (pickInfoVo.getIsFinish() != null) {
            //如果isFinish为true是已完成1，否则为未完成0
            Integer isFinishInt = pickInfoVo.getIsFinish() ? 1 : 0;
            queryWrapper.eq(AnnounceInfo::getIsFinish, isFinishInt);
        }
        if(pickInfoVo.getIsRead() != null){
            //如果isRead为true是已读1，否则为未读0
            Integer isReadInt = pickInfoVo.getIsRead() ? 1 : 0;
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
        Map<Long, String> empNameMap = employeeService.getEmpNameMapByIds(empIds);
        Map<Long, String> groupNameMap = empGroupService.getGroupNameMapByIds(groupIds);
        // 转换records
        List<AnnounceInfoVo> voList = records.stream()
            .map(pickInfo -> {
                AnnounceInfoVo vo = new AnnounceInfoVo();
                BeanUtils.copyProperties(pickInfo, vo);

                // 设置关联的名称信息
                vo.setSenderName(empNameMap.get(pickInfo.getSenderId()));
                vo.setEmpName(empNameMap.get(pickInfo.getEmpId()));
                vo.setGroupName(groupNameMap.get(pickInfo.getGroupId()));
                
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
        
        if (StringUtils.isNotBlank(pickInfoVo.getTitle())) {
            queryWrapper.like(AnnounceInfo::getTitle, pickInfoVo.getTitle());
        }

        if (pickInfoVo.getEmpId() != null) {
            queryWrapper.eq(AnnounceInfo::getEmpId, pickInfoVo.getEmpId());
        }
        if(pickInfoVo.getOrchardId() != null){
            queryWrapper.eq(AnnounceInfo::getOrchardId, pickInfoVo.getOrchardId());
        }
        if (pickInfoVo.getIsFinish() != null) {
            //如果isFinish为true是已完成1，否则为未完成0
            Integer isFinishInt = pickInfoVo.getIsFinish() ? 1 : 0;
            queryWrapper.eq(AnnounceInfo::getIsFinish, isFinishInt);
        }
        if(pickInfoVo.getIsRead() != null){
            //如果isRead为true是已读1，否则为未读0
            Integer isReadInt = pickInfoVo.getIsRead() ? 1 : 0;
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
        for (AnnounceInfo pickInfo : records) {
            if(pickInfo.getSenderId() != null) empIds.add(pickInfo.getSenderId());
            if(pickInfo.getEmpId() != null) empIds.add(pickInfo.getEmpId());
            if(pickInfo.getGroupId() != null) groupIds.add(pickInfo.getGroupId());
        }

        // 3. 批量查询名称（1次DB查询，而非N次）
        Map<Long, String> empNameMap = employeeService.getEmpNameMapByIds(empIds);
        Map<Long, String> groupNameMap = empGroupService.getGroupNameMapByIds(groupIds);

        return pickInfos.stream().map(pickInfo -> {
            AnnounceInfoVo pickInfoVoResult = new AnnounceInfoVo();
            BeanUtils.copyProperties(pickInfo, pickInfoVoResult);
            // 设置关联的名称信息
                pickInfoVoResult.setSenderName(empNameMap.get(pickInfo.getSenderId()));
                pickInfoVoResult.setEmpName(empNameMap.get(pickInfo.getEmpId()));
                pickInfoVoResult.setGroupName(groupNameMap.get(pickInfo.getGroupId()));
            return pickInfoVoResult;
        }).collect(Collectors.toList());
    }

    public String changeInfo(AnnounceInfoVo pickInfoVo){
        LambdaQueryWrapper<AnnounceInfo> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(AnnounceInfo::getId, pickInfoVo.getId());
        AnnounceInfo pickInfo = this.getOne(queryWrapper);
        Long senderId = pickInfoVo.getSenderId();
        Long userId = BaseContext.getId();
        if(senderId != null && senderId != userId){
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
            Long empId = pickInfoVo.getEmpId();
            if(empId != null){
                String empName = employeeService.getEmpNameById(pickInfoVo.getEmpId());
                if(empName == null){
                    return "未获取到当前员工信息，请检查empId是否正常";
                }
            }
        } catch (Exception e) {
            return "未获取到当前员工信息，请检查empId是否正常";
        }
        
        try {
            Long groupId = pickInfoVo.getGroupId();
            if(groupId != null){
                // log.info("groupId:{}",groupId);
                String groupName = empGroupService.getGroupNameById(groupId);
                // log.info("groupName:{}",groupName);
                if(groupName == null){
                    return "未获取到当前组信息，请检查groupId是否正常";
                }
            }
        } catch (Exception e) {
            return "未获取到当前组信息，请检查groupId是否正常";
        }
        try {
            Long orchardId = pickInfoVo.getOrchardId();
            if(orchardId != null){
                String orchardName = orchardServiceImpl.getOrchardNameById(orchardId);
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

    public String updateReadInfo(Long id){
        if(id == null){
            return "未获取到通知id，请检查id是否正常";
        }
        AnnounceInfo info = this.getById(id);
        if(info == null){
            return "通知不存在";
        }
        info.setIsRead(true);
        return this.updateById(info) ? "已读成功" : "已读失败";
    }
    public String updateFinishInfo(Long id){
        if(id == null){
            return "未获取到通知id，请检查id是否正常";
        }
        AnnounceInfo info = this.getById(id);
        if(info == null){
            return "通知不存在";
        }
        info.setIsFinish(true);
        return this.updateById(info) ? "完成成功" : "完成失败";
    }
    public String saveDeployInfo(AnnounceInfoVo pickInfoVo){
        Long senderId = pickInfoVo.getSenderId();
        Long userId = BaseContext.getId();
        if(senderId != userId){
            return R.error("当前用户传来的不是登录用户，请检查登录状态").toString();
        }
        try { 
            if(senderId == null || userId == null){
                return R.error("未获取到当前登录用户信息，请检查登录状态").toString();
            }
            String senderName = employeeService.getEmpNameById(senderId);
            if(senderName == null){
                return R.error("未获取到当前发布者用户信息，请检查senderId是否正常").toString();
            }
        } catch (Exception e) {
            return R.error("未获取到当前员工信息，请检查empId是否正常").toString();
        }
        try {
            Long orchardId = pickInfoVo.getOrchardId();
            if(orchardId == null){
                return R.error("请选择果园").toString();
            }
            String orchardName = orchardServiceImpl.getOrchardNameById(orchardId);
            if(orchardName == null){
                return R.error("未获取到当前果园信息，请检查orchardId是否正常").toString();
            }
        } catch (Exception e) {
            return R.error("未获取到当前果园信息，请检查orchardId是否正常").toString();
        }
        
        AnnounceInfo pickInfo = new AnnounceInfo();
        BeanUtils.copyProperties(pickInfoVo, pickInfo);
        this.save(pickInfo);
        return "发布成功";
    }
}
