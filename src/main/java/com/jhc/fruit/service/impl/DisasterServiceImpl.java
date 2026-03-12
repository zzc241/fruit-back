package com.jhc.fruit.service.impl;

import com.jhc.fruit.common.R;
import com.jhc.fruit.dao.AreaDao;
import com.jhc.fruit.entity.Area;
import com.jhc.fruit.entity.vo.DisasterTypeVo;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.jhc.fruit.dao.DisasterDao;
import com.jhc.fruit.entity.Disaster;
import com.jhc.fruit.entity.vo.DisasterVo;
import com.jhc.fruit.service.DisasterService;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class DisasterServiceImpl  extends ServiceImpl<DisasterDao, Disaster> implements DisasterService{
    @Autowired
    private DisasterDao disasterDao;
    @Autowired
    private AreaDao areaDao;

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

    @Override
    public R<List<DisasterTypeVo>> statisticsDisaster(Long orchardId) {
        List<DisasterTypeVo> disasterTypeList = new ArrayList<>();
        //遍历灾害类别
        List<Long> typeIds = List.of(1L, 2L, 3L);
        for (Long typeId : typeIds) {
            List<DisasterTypeVo.DisasterType> dtList = new ArrayList<>();
            //统计该类型下出现过的灾害
            List<String> typeList = countDisaster(orchardId, typeId);
            //获取对应灾害出现次数
            typeList.forEach(type -> {
                Integer count = disasterDao.getTypeCount(orchardId, type);
                //记录灾害发送次数
                DisasterTypeVo.DisasterType disasterType = new DisasterTypeVo.DisasterType(type, count);
                dtList.add(disasterType);
            });
            //汇总大类下灾害统计记录
            DisasterTypeVo disasterTypeVo = new DisasterTypeVo();
            disasterTypeVo.setTypeId(typeId);
            disasterTypeVo.setTypeCount(dtList);
            //保持该果园的该大类下的灾害统计记录
            disasterTypeList.add(disasterTypeVo);
        }

        return R.success(disasterTypeList);
    }

    @Override
    public R<List<Map<Area, List<DisasterTypeVo>>>> statisticsAreaDisaster(Long orchardId) {
        List<Map<Area, List<DisasterTypeVo>>> result = new ArrayList<>();
        LambdaQueryWrapper<Area> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(Area::getOrchardId, orchardId);
        List<Area> areaList = areaDao.selectList(queryWrapper);
        for (Area area : areaList) {
            Map<Area, List<DisasterTypeVo>> map = new HashMap<>();
            Long areaId = area.getId();
            List<DisasterTypeVo> disasterTypeList = new ArrayList<>();
            //遍历灾害类别
            List<Long> typeIds = List.of(1L, 2L, 3L);
            for (Long typeId : typeIds) {
                List<DisasterTypeVo.DisasterType> dtList = new ArrayList<>();
                //统计该类型下出现过的灾害
                List<String> typeList = countDisaster(orchardId, typeId);
                //获取对应灾害出现次数
                typeList.forEach(type -> {
                    Integer count = disasterDao.getTypeCountArea(orchardId, type, areaId);
                    //记录灾害发送次数
                    DisasterTypeVo.DisasterType disasterType = new DisasterTypeVo.DisasterType(type, count);
                    dtList.add(disasterType);
                });
                //汇总大类下灾害统计记录
                DisasterTypeVo disasterTypeVo = new DisasterTypeVo();
                disasterTypeVo.setTypeId(typeId);
                disasterTypeVo.setTypeCount(dtList);
                //保持该区域的该大类下的灾害统计记录
                disasterTypeList.add(disasterTypeVo);
            }
            map.put(area, disasterTypeList);
            result.add(map);
        }
        return R.success(result);
    }

    /**
     * 统计出现过的灾害类型
     * */
    public List<String> countDisaster(Long orchardId, Long typeId) {
        List<Map<String, Integer>> mapList = new ArrayList<>();
        //查询病虫害（去重后）
        List<String> typeList = disasterDao.selectType(orchardId, typeId);
        return typeList;
    }
    
    
}
