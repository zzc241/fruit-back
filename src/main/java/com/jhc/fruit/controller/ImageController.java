package com.jhc.fruit.controller;

import org.springframework.web.bind.annotation.RestController;

import com.jhc.fruit.service.ImageService;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

@RestController
@RequestMapping("/Image")
@Api(tags = "????????")
public class ImageController {
    @Autowired
    private ImageService imageService;

    @GetMapping("/getRawImage")
    @ApiOperation("???????")
    public List<String> getRawImage() {
        List<String> list = imageService.getRawImage();
        return list;
    }

    @GetMapping("/getHandledImage")
    @ApiOperation("?????????")
    public List<String> getHandledImage() {
        List<String> list = imageService.getHandledImage();
        return list;
    }

}
