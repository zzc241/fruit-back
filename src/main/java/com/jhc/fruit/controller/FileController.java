package com.jhc.fruit.controller;

import com.jhc.fruit.common.QRCodeUtil;
import com.jhc.fruit.common.R;
import com.jhc.fruit.service.FileService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
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
@Api(tags = "文件上传接口")
public class FileController {

    @Autowired
    private FileService fileService;

    /**
     * @param file
     * 返回预览链接
     * */
    @ApiOperation("上传文件")
    @PostMapping("upload")
    public R<String> upload(@RequestPart MultipartFile file) {
        return R.success(fileService.upload(file));
    }

    /**
     * @param fileName
     * 预览图片
     * */
    @ApiOperation("预览文件")
    @GetMapping("preview/{fileName}")
    public void preview(
            @ApiParam(name = "fileName", value = "在Minio的相对路径（xxx.png）", required = true)
            @PathVariable String fileName,
            HttpServletResponse response
    ) {
        try {
            // 对fileName进行URL解码
            String decodedFileName = URLDecoder.decode(fileName, StandardCharsets.UTF_8);
            log.info(decodedFileName);
            fileService.preview(decodedFileName, response);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @ApiOperation("生成二维码")
    @PostMapping("/createQROfUrl")
    public R<String> createQROfUrl(@RequestParam String url) {
        String fileName = fileService.upload(QRCodeUtil.createQROfURL(url));
        return R.success(fileName);
    }

    @ApiOperation("解析二维码")
    @PostMapping("/readQR")
    public R<String> readQR(@RequestParam String qr) {
        String path = QRCodeUtil.readQRCode(fileService.getMinioFileInputStream(qr));
        return R.success(path);
    }

    @ApiOperation("生成果实二维码")
    @PostMapping("/createFruitQR")
    public R<String> createFruitQR(@RequestParam Long id) {
        String fileName = fileService.upload(QRCodeUtil.createFruitQRCode(id));
        return R.success(fileName);
    }

    @ApiOperation("解析果实二维码")
    @PostMapping("/readFruitQR")
    public R<Long> readFruitQR(@RequestParam String qr) {
        Long fruitId = QRCodeUtil.readFruitQRCode(fileService.getMinioFileInputStream(qr));
        return R.success(fruitId);
    }
}
