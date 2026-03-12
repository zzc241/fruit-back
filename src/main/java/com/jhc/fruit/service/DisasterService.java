package com.jhc.fruit.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.jhc.fruit.common.R;
import com.jhc.fruit.entity.Area;
import com.jhc.fruit.entity.Disaster;
import com.jhc.fruit.entity.vo.DisasterTypeVo;
import com.jhc.fruit.entity.vo.DisasterVo;

import java.util.List;
import java.util.Map;

public interface DisasterService extends IService<Disaster> {
    public DisasterVo getByOrchardId(Long orchardId);

    R<List<DisasterTypeVo>> statisticsDisaster(Long orchardId);

    R<List<Map<Area,List<DisasterTypeVo>>>> statisticsAreaDisaster(Long orchardId);
}
