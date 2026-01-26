package com.jhc.fruit.controller;

import com.jhc.fruit.common.R;
import com.jhc.fruit.service.FileService;
import com.jhc.fruit.service.impl.FileServiceImpl;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;

/**
 * @author :zyr
 * @Date : 2026/1/25
 * @description ：minio文件上传下载
 * */

@Slf4j
@RestController
@RequestMapping("/minio")
@Api
public class FileController {

    @Autowired
    private FileServiceImpl fileServiceImpl;

    /**
     * @param file
     * 返回预览链接
     * */
    @ApiOperation("上传文件")
    @PostMapping("upload")
    public R<String> upload(@RequestPart("file") MultipartFile file) {
        return R.success(fileServiceImpl.upload(file));
    }

    /**
     * @param fileName
     * 返回下载链接
     * */
    @ApiOperation("预览文件")
    @GetMapping("preview/{fileName}")
    public void preview(@ApiParam(name = "fileName", value = "在Minio的相对路径（xxx.png）", required = true)
            @PathVariable String fileName,
            HttpServletResponse response) {
        try {
            // 对fileName进行URL解码
            String decodedFileName = URLDecoder.decode(fileName, StandardCharsets.UTF_8);
            log.info(decodedFileName);
            fileServiceImpl.preview(decodedFileName, response);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
