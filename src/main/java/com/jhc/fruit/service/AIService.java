package com.jhc.fruit.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.jhc.fruit.common.R;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface AIService{

    R<List<Integer>> AIOfRipeness(MultipartFile file, double conf, double iou);
}
