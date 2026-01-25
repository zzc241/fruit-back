package com.jhc.fruit.service.impl;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.jhc.fruit.dao.PickInfoDao;
import com.jhc.fruit.entity.PickInfo;
import com.jhc.fruit.entity.vo.getPickInfoVo;
import com.jhc.fruit.service.PickInfoService;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

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
 */
@Service
public class PickInfoServiceImpl extends ServiceImpl<PickInfoDao, PickInfo> implements PickInfoService {
    @Autowired
    private PickInfoDao pickInfoDao;

    @Override
    public Page<PickInfo> getNoFinish(int page, int pageSize, String title, Long empId) {
        //新建分页对象
        Page<PickInfo> pageInfo = new Page<>(page,pageSize);
        LambdaQueryWrapper<PickInfo> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(PickInfo::getIsFinish, 0);
        if(StringUtils.isNotBlank(title)){
            queryWrapper.like(PickInfo::getTitle, title);
        }
        if(empId != null){
            queryWrapper.eq(PickInfo::getEmpId, empId);
        }
        queryWrapper.orderByDesc(PickInfo::getCreateTime);
        //分页查询
        pageInfo = page(pageInfo , queryWrapper);
        return pageInfo;
    }
    @Override
    public Page<PickInfo> getFinish(int page, int pageSize, String title, Long empId){
        Page<PickInfo> pageInfo = new Page<>(page, pageSize);
    
        // 构建查询条件
        LambdaQueryWrapper<PickInfo> queryWrapper = new LambdaQueryWrapper<>();
        
        // 只查询状态为"完成"的记录
        queryWrapper.eq(PickInfo::getIsFinish, 1);
        
        if (StringUtils.isNotBlank(title)) {
            queryWrapper.like(PickInfo::getTitle, title);
        }
        
        if (empId != null) {
            queryWrapper.eq(PickInfo::getEmpId, empId);
        }
        
        // 按时间倒序排列
        queryWrapper.orderByDesc(PickInfo::getCreateTime);
        
        pageInfo = page(pageInfo , queryWrapper);
        return pageInfo;
    }




    @Override
    public Page<PickInfo> getPageInfo(getPickInfoVo getPickInfoVo) { 
        Page<PickInfo> pageInfo = new Page<>(getPickInfoVo.getPage(), getPickInfoVo.getPageSize());
        LambdaQueryWrapper<PickInfo> queryWrapper = new LambdaQueryWrapper<>();

        if (StringUtils.isNotBlank(getPickInfoVo.getTitle())) {
            queryWrapper.like(PickInfo::getTitle, getPickInfoVo.getTitle());
        }

        if (getPickInfoVo.getEmpId() != null) {
            queryWrapper.eq(PickInfo::getEmpId, getPickInfoVo.getEmpId());
        }

        if (getPickInfoVo.getIsFinish() != null) {
            //如果isFinish为true是已完成1，否则为未完成0
            Integer isFinishInt = getPickInfoVo.getIsFinish() ? 1 : 0;
            queryWrapper.eq(PickInfo::getIsFinish, isFinishInt);
        }
        if(getPickInfoVo.getIsRead() != null){
            //如果isRead为true是已读1，否则为未读0
            Integer isReadInt = getPickInfoVo.getIsRead() ? 1 : 0;
            queryWrapper.eq(PickInfo::getIsRead, isReadInt);
        }
        // 按时间倒序排列
        queryWrapper.orderByDesc(PickInfo::getCreateTime);
        pageInfo = page(pageInfo , queryWrapper);
        return pageInfo;
    }
    @Override
    public List<PickInfo> getInfo(String title, Long empId , Boolean isFinish , Boolean isRead) {
        // 构建查询条件
        LambdaQueryWrapper<PickInfo> queryWrapper = new LambdaQueryWrapper<>();
        
        if (StringUtils.isNotBlank(title)) {
            queryWrapper.like(PickInfo::getTitle, title);
        }
        
        if (empId != null) {
            queryWrapper.eq(PickInfo::getEmpId, empId);
        }

        if (isFinish != null) {
            //如果isFinish为true是已完成1，否则为未完成0
            Integer isFinishInt = isFinish ? 1 : 0;
            queryWrapper.eq(PickInfo::getIsFinish, isFinishInt);
        }
        if(isRead != null){
            //如果isRead为true是已读1，否则为未读0
            Integer isReadInt = isRead ? 1 : 0;
            queryWrapper.eq(PickInfo::getIsRead, isReadInt);
        }
        
        // 按时间倒序排列
        queryWrapper.orderByDesc(PickInfo::getCreateTime);

        // 限制最多返回500条，避免数据过大
        queryWrapper.last("LIMIT 500");
        return this.list(queryWrapper);
    }
}
