package com.jhc.fruit.controller;

import com.jhc.fruit.common.R;
import com.jhc.fruit.entity.AIWeeds;
import com.jhc.fruit.service.AIService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
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

    @ApiOperation("成熟果实数量统计")
    @PostMapping("/RipeCount")
    public R<List<Integer>> AIripe(@RequestParam MultipartFile file,
                                   @ApiParam(value = "conf", defaultValue = "0.25")
                                   double conf,
                                   @ApiParam(value = "iou", defaultValue = "0.5")
                                   double iou) {

        return aiService.AIOfRipeness(file, conf, iou);
    }

    @ApiOperation("YOLOv11 目标检测接口")
    @PostMapping("/YOLOv11Weed")
    public R<List<AIWeeds.WeedDetection>> AIWeed(MultipartFile weedFile){
        return aiService.AIWeed(weedFile);
    }
}
