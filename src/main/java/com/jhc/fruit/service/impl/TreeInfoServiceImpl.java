package com.jhc.fruit.service.impl;

import com.jhc.fruit.dao.TreeInfoDao;
import com.jhc.fruit.entity.vo.TreeInfoVo;
import com.jhc.fruit.service.TreeInfoService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

/**
 * 果树信息服务实现类
 */
@Service
public class TreeInfoServiceImpl implements TreeInfoService {

    // 注入Dao层，解决标红问题
    @Resource
    private TreeInfoDao treeInfoDao;

    @Override
    public TreeInfoVo getTreeInfoByTreeId(String treeId) {
        // 校验入参（可选，增强健壮性）
        if (treeId == null || treeId.trim().isEmpty()) {
            throw new IllegalArgumentException("treeId不能为空");
        }
        return treeInfoDao.selectTreeInfoByTreeId(treeId);
    }
}