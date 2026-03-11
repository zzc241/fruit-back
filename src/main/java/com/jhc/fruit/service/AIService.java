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

    R<AIWeeds.WeedDetectionResponse> AIWeed(MultipartFile weedFile);

    DisasterVo weatherAlert(Long orchardId);

    String chat(String query);

    String analyzeWaterFertilizer(MultipartFile msFile , MultipartFile rgbFile);

    R<DiseaseResponse> identifyDisease(MultipartFile maturityFile);
    
    R<MaturityResponse> identifyMaturity(MultipartFile maturityFile);
}
