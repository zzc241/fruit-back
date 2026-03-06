package com.jhc.fruit.service;

import com.jhc.fruit.entity.vo.TreeInfoVo;

public interface TreeInfoService {
    /**
     * 根据 tree_id 获取果树完整信息
     */
    TreeInfoVo getTreeInfoByTreeId(String treeId);
}
