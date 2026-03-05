package com.jhc.fruit.service.impl;

import java.util.List;

import org.springframework.stereotype.Service;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.jhc.fruit.dao.TaskImgDao;
import com.jhc.fruit.entity.TaskImg;
import com.jhc.fruit.service.TaskImgService;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class TaskImgServiceImpl extends ServiceImpl<TaskImgDao , TaskImg> implements TaskImgService{

    @Override
    public void addTaskImg(TaskImg taskImg) {
        log.info("添加任务图片");
        this.save(taskImg);
    }

    @Override
    public void removeByTaskId(Long taskId) {
        log.info("删除任务图片");
        LambdaQueryWrapper<TaskImg> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(TaskImg::getTaskId, taskId);
        this.remove(queryWrapper);
    }

    @Override
    public List<String> getImg(Long taskId){
        log.info("获取任务图片");
        LambdaQueryWrapper<TaskImg> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(TaskImg::getTaskId, taskId);
        List<TaskImg> taskImgs = this.list(queryWrapper);
        List<String> imgs = taskImgs.stream().map(TaskImg::getImg).toList();
        return imgs;
    }
}
