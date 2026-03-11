package com.jhc.fruit.service.impl;

import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.jhc.fruit.dao.DisasterDao;
import com.jhc.fruit.entity.Disaster;
import com.jhc.fruit.entity.vo.DisasterVo;
import com.jhc.fruit.service.DisasterService;

@Service
public class DisasterServiceImpl  extends ServiceImpl<DisasterDao, Disaster> implements DisasterService{

    @Override
    public DisasterVo getByOrchardId(Long orchardId) {
        //按照时间倒序查询
        LambdaQueryWrapper<Disaster> queryWrapper = new LambdaQueryWrapper<Disaster>()
                .eq(Disaster::getOrchardId, orchardId)
                .orderByDesc(Disaster::getCreateTime)
                .last("limit 1");
        Disaster disaster = this.getOne(queryWrapper);


        if (disaster == null) {
            return null; // 或者抛出一个异常，表示未找到对应的灾害信息
        }
        DisasterVo disasterVo = new DisasterVo();
        BeanUtils.copyProperties(disaster, disasterVo);
        return disasterVo;
    }
    
    
}
