package com.jhc.fruit.service.impl;

import java.util.List;
import java.util.stream.Collectors;

import com.jhc.fruit.common.R;
import com.jhc.fruit.entity.Area;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.jhc.fruit.dao.OrchardDao;
import com.jhc.fruit.entity.Orchard;
import com.jhc.fruit.entity.vo.OrchardVo;
import com.jhc.fruit.service.AreaService;
import com.jhc.fruit.service.EmployeeService;
import com.jhc.fruit.service.OrchardService;

import cn.hutool.core.util.StrUtil;
import lombok.extern.slf4j.Slf4j;

/**
 * @author ：zzc
 * @description：果园管理服务实现类
 * @date ：2026/1/26
 */
@Slf4j
@Service
public class OrchardServiceImpl extends ServiceImpl<OrchardDao, Orchard> implements OrchardService{
    @Autowired
    private AreaService areaService;
    @Autowired
    private EmployeeService employeeService;
    @Autowired
    private OrchardDao orchardDao;

    /*
     * 
     * {
        "areaId": 1,
        "areaName": "null",
        "description": "null",
        "empId": 1,
        "empName": "null",
        "id": 1,
        "name": "null",
        "page": 1,
        "pageSize": 10,
        "size": 1,
        "status": 1
        }
     */
    @Override
    public Page<OrchardVo> getPageInfo(OrchardVo orchardVo) { 
        Page<Orchard> pageInfo = new Page<>(orchardVo.getPage(), orchardVo.getPageSize());
        LambdaQueryWrapper<Orchard> queryWrapper = new LambdaQueryWrapper<>();
        Long id = orchardVo.getId();
        if(id != null){
            queryWrapper.eq(Orchard::getId, id);
        }
        //empId是本地登录员工id，固定查询当前员工的果园信息
        if(orchardVo.getEmpId() != null){
            queryWrapper.eq(Orchard::getEmpId, orchardVo.getEmpId());
        }
        //查询果园名字
        if(orchardVo.getName() != null){
            queryWrapper.like(Orchard::getName, orchardVo.getName());
        }
        //根据areaId查询果园
        // String areaName = orchardVo.getAreaName();
        //根据area名称查询areaId
        // if (orchardVo.getAreaId() != null) {
        //     queryWrapper.eq(Orchard::getAreaId, orchardVo.getAreaId());
        // }
        // else if(StringUtils.isNotBlank(areaName)) {
        //     Long areaId = areaService.getAreaIdByName(areaName);
        //     queryWrapper.eq(Orchard::getAreaId, areaId);
        // }
        //根据果园大小查询果园
        Long size = orchardVo.getSize();
        if(size != null){
            queryWrapper.eq(Orchard::getSize, size);
        }
        //根据状态查询果园
        //这里做异常捕获，防止前端传入null值报错，现在即使传入null值也不会报错
        try {
            int status = orchardVo.getStatus();
            // log.info("status: {}", status);
            queryWrapper.eq(Orchard::getStatus, status);
        } catch (Exception e) {
            log.info("获取状态信息失败，请稍后重试");
        }
        String empName = orchardVo.getEmpName();
        if (orchardVo.getEmpId() != null) {
            queryWrapper.eq(Orchard::getEmpId, orchardVo.getEmpId());
        }
        else if(StrUtil.isNotBlank(empName)) {
            Long empId = employeeService.getEmpIdByName(empName);
            queryWrapper.eq(Orchard::getEmpId, empId);
        }
        
        // 按时间倒序排列
        queryWrapper.orderByDesc(Orchard::getCreateTime);
        pageInfo = page(pageInfo , queryWrapper);

        //上面是查询数据Orchard，下面是转换数据OrchardVO
        // 转换分页数据
        Page<OrchardVo> pageInfoVo = new Page<>(
            pageInfo.getCurrent(),
            pageInfo.getSize(),
            pageInfo.getTotal()
        );
        pageInfoVo.setPages(pageInfo.getPages());

        // 转换records
        List<OrchardVo> voList = pageInfo.getRecords().stream()
            .map(orchard -> {
                OrchardVo vo = new OrchardVo();
                BeanUtils.copyProperties(orchard, vo);
                
                // 设置关联的名称信息
                // if (orchard.getAreaId() != null) {
                //     String areaNameTemp = areaService.getAreaNameById(orchard.getAreaId());
                //     vo.setAreaName(areaNameTemp);
                // }
                
                if (orchard.getEmpId() != null) {
                    // 假设有employeeService可以获取员工姓名
                    String empNameTemp = employeeService.getEmpNameById(orchard.getEmpId());
                    vo.setEmpName(empNameTemp);
                }
                
                return vo;
            })
            .collect(Collectors.toList());
        
        pageInfoVo.setRecords(voList);

        return pageInfoVo;
    }

    /**
     * 
     * {
        "areaId": 1,
        "areaName": "null",
        "description": "null",
        "id": 1,
        "name": "null",
        "size": 1,
        "status": 1
        }
     */
    @Override
    public List<OrchardVo> getInfo(OrchardVo orchardVo) {
        // 构建查询条件
        LambdaQueryWrapper<Orchard> queryWrapper = new LambdaQueryWrapper<>();
        Long id = orchardVo.getId();
        if(id != null){
            queryWrapper.eq(Orchard::getId, id);
        }
        //empId是本地登录员工id，固定查询当前员工的果园信息
        queryWrapper.eq(Orchard::getEmpId, orchardVo.getEmpId());
        //查询果园名字
        if(orchardVo.getName() != null){
            queryWrapper.like(Orchard::getName, orchardVo.getName());
        }
        //根据areaId查询果园
        // String areaName = orchardVo.getAreaName();
        //根据area名称查询areaId
        // if (orchardVo.getAreaId() != null) {
        //     queryWrapper.eq(Orchard::getAreaId, orchardVo.getAreaId());
        // }
        // else if(StringUtils.isNotBlank(areaName)) {
        //     Long areaId = areaService.getAreaIdByName(areaName);
        //     queryWrapper.eq(Orchard::getAreaId, areaId);
        // }
        //根据果园大小查询果园
        Long size = orchardVo.getSize();
        if(size != null){
            queryWrapper.eq(Orchard::getSize, size);
        }
        //根据状态查询果园
        //这里做异常捕获，防止前端传入null值报错，现在即使传入null值也不会报错
        try {
            int status = orchardVo.getStatus();
            // log.info("status: {}", status);
            queryWrapper.eq(Orchard::getStatus, status);
        } catch (Exception e) {
            log.info("获取状态信息失败，请稍后重试");
        }
        
        queryWrapper.orderByDesc(Orchard::getCreateTime);
        List<Orchard> orchards = this.list(queryWrapper);
        return orchards.stream().map(orchard -> {
            OrchardVo orchardVoResult = new OrchardVo();
            BeanUtils.copyProperties(orchard, orchardVoResult);
            // if (orchard.getAreaId() != null) {
            //     String areaNameCur = areaService.getAreaNameById(orchard.getAreaId());
            //     orchardVoResult.setAreaName(areaNameCur);
            // }
            if (orchard.getEmpId() != null) {
                String empNameCur = employeeService.getEmpNameById(orchard.getEmpId());
                orchardVoResult.setEmpName(empNameCur);
            }
            return orchardVoResult;
        }).collect(Collectors.toList());
    }

    /**
     * 添加果园信息
     * @param orchardVo
     * @return
     * {
        "areaId": 2,
        "description": "string",
        "empId": 1,
        "name": "测试3size",
        "size": 18,
        "status": 1
        }
     */
    public Long saveOrchardInfo(OrchardVo orchardVo){
        Orchard orchard = new Orchard();
        // Long empId = orchardVo.getEmpId();
        // if(empId == null){
        //     return "员工ID不能为空";
        // }
        // try {
        //     employeeService.getEmpNameById(empId);
        // } catch (Exception e) {
        //     return "获取员工信息失败，请稍后重试";
        // }
        // Long size = orchardVo.getSize();
        // if(size <= 0){
        //     return "果园大小不能小于0";
        // }
        // String name = orchardVo.getName();
        // if(StringUtils.isBlank(name)){
        //     return "果园名称不能为空";
        // }
        // int status = orchardVo.getStatus();
        // if(status != 0){
        //     return "果园状态不合法";
        // }
        BeanUtils.copyProperties(orchardVo, orchard);
        this.save(orchard);
        Long id = orchard.getId();
        return id;
    }
    /**
     * {
        "areaId": 1,
        "description": "修改了吗2222",
        "empId": 1,
        "id": 1,
        "name": "string",
        "size": 11110,
        "status": 1
        }
     */
    public String changeInfo(OrchardVo orchardVo){
        Orchard orchard = new Orchard();
        Long id = orchardVo.getId();
        if(id == null){
            return "ID不能为空";
        }
        Long empId = orchardVo.getEmpId();
        if(empId == null){
            return "员工ID不能为空";
        }
        try {
            employeeService.getEmpNameById(empId);
        } catch (Exception e) {
            return "获取员工信息失败，请稍后重试";
        }
        Long size = orchardVo.getSize();
        if(size <= 0){
            return "果园大小不能小于0";
        }
        int status = orchardVo.getStatus();
        if(status == 1){
            //只有管理员可以将果园状态改为1（通过审核）
            //TODO这里判断是否是超级管理员
        }
        BeanUtils.copyProperties(orchardVo, orchard);
        this.updateById(orchard);
        return "修改成功";
    }

    public String getOrchardNameById(Long orchardId){
        return this.getById(orchardId).getName();
    }

    public R<String> updateByOrchardVo(OrchardVo orchardVo) {
        Orchard orchard = new Orchard();
        BeanUtils.copyProperties(orchardVo, orchard);
        orchardDao.insert(orchard);
        return R.success("修改成功");
    }

    public Boolean removeOrchard(Long id) {
        try {
            List<Area> areaList = areaService.selectByOrchardId(id);
            for(Area area : areaList){
                areaService.removeArea(area.getId());
            }
            orchardDao.deleteById(id);
            return true;
        }catch (Exception e){
            log.info(e.getMessage());
            return false;
        }
    }

    @Override
    public String getOrchardNameByEmpId(Long empId) {
        return "";
    }
}