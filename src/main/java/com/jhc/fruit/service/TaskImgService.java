package com.jhc.fruit.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.jhc.fruit.entity.TaskImg;

@Service
public interface TaskImgService extends IService<TaskImg> {

    void addTaskImg(TaskImg taskImg);

    void removeByTaskId(Long taskId);
    
    List<String> getImg(Long taskId);
}
