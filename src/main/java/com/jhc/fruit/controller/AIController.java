package com.jhc.fruit.controller;

import com.google.gson.JsonObject;
import com.jhc.fruit.common.R;
import com.jhc.fruit.entity.AIWeeds;
import com.jhc.fruit.entity.vo.DisasterVo;
import com.jhc.fruit.entity.vo.DiseaseResponse;
import com.jhc.fruit.entity.vo.MaturityResponse;
import com.jhc.fruit.service.AIService;
import com.jhc.fruit.service.DisasterService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@RequestMapping("/AI")
@Api(tags = "AI接口")
public class AIController {
    @Autowired
    private AIService aiService;
    @Autowired
    private DisasterService disasterService;

    @ApiOperation("YOLOv11 目标检测接口")
    @PostMapping("/YOLOv11Weed")
    public R<List<AIWeeds.WeedDetection>> AIWeed(MultipartFile weedFile){
        return aiService.AIWeed(weedFile);
    }

    @ApiOperation("天气预警接口")
    @GetMapping("/weather-alert")
    public R<DisasterVo> weatherAlert(Long orchardId) {
        DisasterVo disasterVo = disasterService.getByOrchardId(orchardId);
        if (disasterVo != null) {
            return R.success(disasterVo);
        }
        disasterVo = aiService.weatherAlert(orchardId);
        return disasterVo == null ? R.error("没有查询到数据") : R.success(disasterVo);
    }

    @ApiOperation("AI聊天接口")
    @PostMapping("/chat")
    public R<String> chat(@RequestBody String query) {
        String result = aiService.chat(query);
        return R.success(result);
    }

    @ApiOperation("AI聊天接口(JSON)")
    @PostMapping("/chatJSON")
    public R<String> chatJSON(@RequestBody JsonObject query) {
        String str = query.toString();
        String result = aiService.chat(str);
        return R.success(result);
    }

    // @ApiOperation("分析图片接口")
    // @PostMapping("/identify-image")
    // public R<AIWeeds.WeedDetectionResponse> identifyImage(MultipartFile weedFile){
    //     return aiService.AIWeed(weedFile);
    // }
    @ApiOperation("分析病虫害接口")
    @PostMapping("/identify-disease")
    public R<DiseaseResponse> identifyDisease(@RequestParam MultipartFile diseaseFile){
        return aiService.identifyDisease(diseaseFile);
    }
    @ApiOperation("分析成熟度接口")
    @PostMapping("/identify-maturity")
    public R<MaturityResponse> identifyMaturity(MultipartFile maturityFile) {
        return aiService.identifyMaturity(maturityFile);
    }

    @ApiOperation("分析水肥需求接口")
    @PostMapping("/analyze-water-fertilizer")
    public R<String> analyzeWaterFertilizer(MultipartFile msFile , MultipartFile rgbFile){
        String result = aiService.analyzeWaterFertilizer(msFile , rgbFile);
        return R.success(result);
    }

    @ApiOperation("小程序：分析水肥需求接口")
    @PostMapping("/unapp/analyze-water-fertilizer")
    public R<String> unappanalyzeWaterFertilizer(@RequestParam String msFile, @RequestParam String rgbFile){
        String result = aiService.unanalyzeWaterFertilizer(msFile , rgbFile);
        return R.success(result);
    }


}
