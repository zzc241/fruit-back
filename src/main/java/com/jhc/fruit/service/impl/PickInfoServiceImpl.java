package com.jhc.fruit.service.impl;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.jhc.fruit.dao.PickInfoDao;
import com.jhc.fruit.entity.PickInfo;
import com.jhc.fruit.service.PickInfoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author ：lcy
 * @description：TODO
 * @date ：2022/5/30 0030 10:07
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
    public List<PickInfo> getFinish(Page pageInfo, String title, Long empId) {
        List<PickInfo> finishInfo = pickInfoDao.getFinish(pageInfo, title, empId);
        return finishInfo;
    }

    @Override
    public List<PickInfo> getNoFinish(Page pageInfo, String title, Long empId) {
        List<PickInfo> noFinishInfo = pickInfoDao.getNoFinish(pageInfo, title, empId);
        return noFinishInfo;
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
}
