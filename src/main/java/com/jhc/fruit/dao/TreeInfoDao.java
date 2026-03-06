package com.jhc.fruit.dao;

import com.jhc.fruit.entity.vo.TreeInfoVo;
import org.apache.ibatis.annotations.Param;

public interface TreeInfoDao {
    /**
     * 根据 tree_id 查询果树完整信息
     */
    TreeInfoVo selectTreeInfoByTreeId(@Param("treeId") String treeId);
}