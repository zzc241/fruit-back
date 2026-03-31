package com.jhc.fruit.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.jhc.fruit.dao.ImageDao;
import com.jhc.fruit.service.ImageService;

@Service
public class ImageServiceImpl extends ServiceImpl<ImageDao, String> implements ImageService {

    @Autowired
    private ImageDao imageDao;
    @Override
    public List<String> getRawImage(){
        List<String> list = imageDao.getRawImage();
        return list;

    }
    @Override
    public List<String> getHandledImage(){
        List<String> list = imageDao.getHandledImage();
        return list;
        

    }
}
