package com.jhc.fruit.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.jhc.fruit.entity.PickInfo;

import java.util.List;

/**
 * @author ：lcy
 * @description：TODO
 * @date ：2022/5/30 0030 10:04
 */
public interface PickInfoService extends IService<PickInfo> {
    public List<PickInfo> getFinish(Page pageInfo, String title, Long empId);
    public List<PickInfo> getNoFinish(Page pageInfo, String title, Long empId);
    public List<PickInfo> getMine(Long empId);
    public Boolean getIsFinish(Long infoId);
    public PickInfo getById(Long id);
    public Page<PickInfo> page(Page<PickInfo> page);
}
