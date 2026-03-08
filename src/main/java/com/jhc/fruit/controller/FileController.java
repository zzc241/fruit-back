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
import java.util.List;

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
    @PostMapping("/createQROfString")
    public R<String> createQROfUrl(@RequestBody String str) {
        String fileName = fileService.upload(QRCodeUtil.createQROfString(str));
        return R.success(fileName);
    }

    @ApiOperation("解析二维码")
    @PostMapping("/readQR")
    public R<String> readQR(@RequestBody String qr) {
        String path = QRCodeUtil.readQRCode(fileService.getMinioFileInputStream(qr));
        return R.success(path);
    }


    /**
     * 获取图片临时访问URL
     * @param fileName MinIO中的图片文件名
     * @return 带过期时间的预签名URL
     */
    @ApiOperation("获取图片临时访问URL")
    @GetMapping("/image/url/{fileName}")
    public R<String> getImageUrl(@PathVariable String fileName) {
        String url = fileService.getImageUrl(fileName);
        return R.success(url);
    }

    @ApiOperation("批量获取url")
    @PostMapping("/image/url/files")
    public R<List<String>> getImageUrl(@RequestBody List<String> files) {
        List<String> urls = fileService.getImageUrls(files);
        if (urls == null || urls.size() == 0){
            return R.error("无数据");
        }
        return R.success(urls);
    }

    @ApiOperation("保存用户头像")
    @PostMapping("/saveHeadImg")
    public R<String> saveHeadImg(@RequestParam("file") MultipartFile file) {
        String fileUrl = fileService.save(file, "headImg");
        return R.success(fileUrl);
    }
}
