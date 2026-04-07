package com.jhc.fruit.controller;

import com.jhc.fruit.common.R;
import com.jhc.fruit.entity.AIWeeds;
import com.jhc.fruit.entity.Analyze;
import com.jhc.fruit.entity.ApiResponse;
import com.jhc.fruit.entity.AudioRecognitionSuccess;
import com.jhc.fruit.entity.vo.*;
import com.jhc.fruit.service.AIService;
import com.jhc.fruit.service.DisasterService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
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

    @ApiOperation("杂草识别")
    @PostMapping("/YOLOv11Weed")
    public R<AIWeedVo> AIWeed(MultipartFile weedFile){
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
    public R<String> unappanalyzeWaterFertilizer(@RequestParam MultipartFile rgbFile){
        String result = aiService.unAnalyzeWaterFertilizer(rgbFile);
        return R.success(result);
    }

    @ApiOperation("WAV音频文件转文字")
    @PostMapping("/processAudio")
    public R<AudioRecognitionSuccess> processAudio (@RequestParam MultipartFile wavFile){
        return aiService.processAudio(wavFile);
    }

    @ApiOperation("多光谱照片分析")
    @PostMapping(value = "/analyze", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ApiResponse analyze(@RequestParam("images") List<MultipartFile> images) {
        try {
            return aiService.analyzeMultiSpectralImages(images);
        } catch (IllegalArgumentException e) {
            ApiResponse response = new ApiResponse();
            response.setCode(400);
            response.setMsg(e.getMessage());
            return response;
        } catch (Exception e) {
            ApiResponse response = new ApiResponse();
            response.setCode(500);
            response.setMsg("分析失败：" + e.getMessage());
            return response;
        }
    }

    @ApiOperation("多光谱照片分析并返回建议")
    //路由
    @PostMapping(value = "/analyzePlus/{orchardId}/{areaId}", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    //接收参数：图片组、区域id
    public ApiResponse analyzePlus(@RequestParam("images") List<MultipartFile> images, @PathVariable Long orchardId, @PathVariable Long areaId) {
        try {
            //调用接口实现类
            ApiResponse apiResponse = aiService.analyzeMultiSpectralImagesPlus(images, orchardId, areaId);
            return apiResponse;
            //抛出异常
        } catch (IllegalArgumentException e) {
            ApiResponse response = new ApiResponse();
            response.setCode(400);
            response.setMsg(e.getMessage());
            return response;
        } catch (Exception e) {
            ApiResponse response = new ApiResponse();
            response.setCode(500);
            response.setMsg("分析失败：" + e.getMessage());
            return response;
        }
    }

    @ApiOperation("获取果园最新多光谱数据")
    @GetMapping("/getAnalyze/{orchardId}/{areaId}")
    public R<List<AnalyzeVo>> getAnalyze(@PathVariable Long orchardId, @PathVariable Long areaId) {
        return aiService.getAnalyze(orchardId, areaId);
    }

    @ApiOperation("获取区域分析数据")
    @GetMapping("/getAnalyzeRegion/{areaId}")
    public R<List<AnalyzeVo>> getAnalyzeRegion(@PathVariable Long areaId) {
        return  aiService.getAnalyzeRegison(areaId);
    }

    @GetMapping("/water/deficit")
    public String getWaterDeficit(
            @RequestParam(required = false) String regionId,
            @RequestParam(required = false) String orchardId) {
        return aiService.getWaterDeficit(regionId, orchardId);
    }

    @GetMapping("/water/spectral")
    public String getSpectralData(
            @RequestParam(required = false) String regionId,
            @RequestParam(required = false) String orchardId) {
        return aiService.getSpectralData(regionId, orchardId);
    }

    @GetMapping("/AI/analyzePlus/{plotId}")
    public String getPlotAnalyzeReport(@PathVariable String plotId) {
        return aiService.getPlotAnalyzeReport(plotId);
    }

}
