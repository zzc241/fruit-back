package com.jhc.fruit.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.jhc.fruit.entity.Disaster;
import com.jhc.fruit.entity.vo.DisasterVo;

public interface DisasterService extends IService<Disaster> {
    public DisasterVo getByOrchardId(Long orchardId);
    
}
