package com.jhc.fruit.controller;

import com.jhc.fruit.entity.vo.TreeInfoVo;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.jhc.fruit.service.TreeInfoService;
import javax.annotation.Resource;

/**
 * 果树信息接口
 */
@RestController
@RequestMapping("/api/tree")
public class TreeInfoController {

    @Resource
    private TreeInfoService treeInfoService;

    /**
     * 根据tree_id查询果树完整信息
     * @param treeId 果树唯一标识
     * @return 果树完整信息
     */
    @GetMapping("/{treeId}")
    public TreeInfoVo getTreeInfo(@PathVariable String treeId) {
        return treeInfoService.getTreeInfoByTreeId(treeId);
    }
}