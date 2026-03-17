package com.jhc.fruit.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.jhc.fruit.common.R;
import com.jhc.fruit.entity.AIWeeds;
import com.jhc.fruit.entity.ApiResponse;
import com.jhc.fruit.entity.AudioRecognitionSuccess;
import com.jhc.fruit.entity.vo.DisasterVo;
import com.jhc.fruit.entity.vo.DiseaseResponse;
import com.jhc.fruit.entity.vo.MaturityResponse;

import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

public interface AIService extends IService<AIWeeds.WeedDetection> {

    R<List<AIWeeds.WeedDetection>> AIWeed(MultipartFile weedFile);

    String AIWeedImg(String originalMinioPath,
                            String outputPrefix,
                            List<AIWeeds.WeedDetection> detections);

    DisasterVo weatherAlert(Long orchardId);

    String chat(String query);

    R<DiseaseResponse> identifyDisease(MultipartFile diseaseFile);

    R<MaturityResponse> identifyMaturity(MultipartFile maturityFile);

    String analyzeWaterFertilizer(MultipartFile msFile, MultipartFile rgbFile);

    String unanalyzeWaterFertilizer(String msFile, String rgbFile);

    R<AudioRecognitionSuccess> processAudio(MultipartFile wavFile);

    ApiResponse analyzeMultiSpectralImages(List<MultipartFile> images) throws IOException;
}
