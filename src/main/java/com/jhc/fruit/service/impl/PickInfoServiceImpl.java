package com.jhc.fruit.service.impl;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.jhc.fruit.dao.PickInfoDao;
import com.jhc.fruit.entity.PickInfo;
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
@Service
public class PickInfoServiceImpl extends ServiceImpl<PickInfoDao, PickInfo> implements PickInfoService {
    @Autowired
    private PickInfoDao pickInfoDao;

    @Override
    public Page<PickInfo> page(Page<PickInfo> page) {
        Page<PickInfo> pageInfo = pickInfoDao.page(page);
        return pageInfo;
    }

    @Override
    public PickInfo getById(Long id) {
        PickInfo pickInfo = pickInfoDao.getById(id);
        return pickInfo;
    }

    @Override
    public Boolean getIsFinish(Long infoId) {
        Boolean isFinish = pickInfoDao.getIsFinish(infoId);
        return isFinish;
    }

    @Override
    public List<PickInfo> getMine(Long empId) {
        List<PickInfo> mine = pickInfoDao.getMine(empId);
        return mine;
    }

//    @Override
//    public List<PickInfo> getAll(Page pageInfo, Long empId, String title) {
//        List<PickInfo> infos = pickInfoDao.getAll(pageInfo,empId,title);
//        return infos;
//    }
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
        Page<PickInfo> noFinishInfo = page(pageInfo , queryWrapper);
        return noFinishInfo;
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
        
        Page<PickInfo> finishInfo = page(pageInfo , queryWrapper);
        return finishInfo;
    }
}
