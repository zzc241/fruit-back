package com.jhc.fruit.service;

import java.util.List;

import com.baomidou.mybatisplus.extension.service.IService;

public interface ImageService extends IService<String>{
    public List<String> getRawImage();

    public List<String> getHandledImage();
}
