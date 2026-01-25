package com.jhc.fruit.controller;

import com.jhc.fruit.common.R;
import com.jhc.fruit.service.FileService;
import com.jhc.fruit.service.impl.FileServiceImpl;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

@Slf4j
@RestController
@RequestMapping("/minioFile")
@Api
public class FileController {

    @Autowired
    private FileService fileService;

    @ApiOperation("上传文件minio")
    @PostMapping("upload")
    public R<String> upload(@RequestPart("file") MultipartFile file) {
        return R.success(fileService.upload(file));
    }
}
