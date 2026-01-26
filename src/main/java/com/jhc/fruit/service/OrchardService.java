package com.jhc.fruit.service;

import java.util.List;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.jhc.fruit.entity.Orchard;
import com.jhc.fruit.entity.vo.OrchardVo;

public interface OrchardService extends IService<Orchard>{

    public Page<OrchardVo> getPageInfo(OrchardVo orchardVo);
    public List<OrchardVo> getInfo(OrchardVo orchardVo);
    public String saveOrchardInfo(OrchardVo orchardVo);
    public String changeInfo(OrchardVo orchardVo);
}
