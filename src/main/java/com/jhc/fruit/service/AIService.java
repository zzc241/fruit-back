package com.jhc.fruit.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.jhc.fruit.common.R;
import com.jhc.fruit.entity.AIWeeds;
import com.jhc.fruit.entity.vo.DisasterVo;
import com.jhc.fruit.entity.vo.DiseaseResponse;
import com.jhc.fruit.entity.vo.MaturityResponse;

import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface AIService extends IService<AIWeeds.WeedDetection> {

    R<List<Integer>> AIOfRipeness(MultipartFile file, double conf, double iou);

    R<List<AIWeeds.WeedDetection>> AIWeed(MultipartFile weedFile);
    
    String AIWeedImg(String originalMinioPath,
                            String outputPrefix,
                            List<AIWeeds.WeedDetection> detections);

    DisasterVo weatherAlert(Long orchardId);

    String chat(String query);

    R<DiseaseResponse> identifyDisease(MultipartFile diseaseFile);

    R<MaturityResponse> identifyMaturity(MultipartFile maturityFile);

    String analyzeWaterFertilizer(MultipartFile msFile, MultipartFile rgbFile);
}
