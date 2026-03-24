package com.jhc.fruit.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.jhc.fruit.entity.FruitTree;
import com.jhc.fruit.entity.Task;
import com.jhc.fruit.entity.vo.TaskVo;
import org.apache.ibatis.annotations.Mapper;

import java.time.LocalDateTime;
import java.util.List;

@Mapper
public interface TaskDao extends BaseMapper<Task> {
    List<Task> selectByOrchardId(Long OrchardId);

    List<Task> getOwnTask(Long empId);

    List<String> getType(Long empId);

    Integer countType(Long empId, String type);

    List<String> getFertType(Long empId);

    Double getfertUseCount(Long empId, String fertType);

    List<String> getpesticideType(Long empId);

    Double getPesticideUseCount(Long empId, String pesticideType);

    List<Task> getMyTask(Long orchardId, Long empId);

    List<Task> getTaskByType(Long orchardId, String type,Long empId);

    List<FruitTree> getTreeByAreaId(Long areaId);

    String getOrchardName(Long orchardId);

    String getReceiverName(Long receiverId);

    String selectRecName(Task task);
}
