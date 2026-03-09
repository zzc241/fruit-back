package com.jhc.fruit.controller;

import com.jhc.fruit.common.R;
import com.jhc.fruit.entity.Report;
import com.jhc.fruit.entity.vo.ReportVo;
import com.jhc.fruit.service.ReportService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

/**
 * 上报信息
 * 扫描果树后扫码上报
 * */
@Slf4j
@RestController
@RequestMapping("/report")
@Api("上报接口")
public class ReportController {

    @Autowired
    private ReportService reportService;

    @ApiOperation("新增(带图片文件)")
    @PostMapping(value = "/addImg", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public R<Long> addImg(
            // 接收业务参数（JSON格式，form-data中以File类型上传JSON文件）
            @RequestPart("report") Report report,
            // 接收各类图片文件列表（form-data中以File类型上传）
            @RequestPart(required = false) List<MultipartFile> bcImgs,
            @RequestPart(required = false) List<MultipartFile> fertWaterImgs,
            @RequestPart(required = false) List<MultipartFile> ripeImgs,
            @RequestPart(required = false) List<MultipartFile> weedsImgs
    ) {

        return reportService.addImg(report, bcImgs, fertWaterImgs, ripeImgs, weedsImgs);
    }

    @ApiOperation("新增")
    @PostMapping( "/add")
    public R<Long> add(
            @ApiParam(value = "报告", required = true)
            @RequestBody ReportVo reportVo) {

        return reportService.add(reportVo);
    }

    @ApiOperation("修改")
    @PostMapping("/update")
    public R<String> update(@RequestBody ReportVo reportVo) {
        reportService.update(reportVo);
        return R.success();
    }

    @ApiOperation("删除")
    @DeleteMapping("/delete")
    public R<String> delete(@RequestBody List<Long> ids) {
        R<String> r = reportService.removeReport(ids);
        return R.success();
    }

    @ApiOperation("查询")
    @PostMapping("/select")
    public R<List<ReportVo>> select(@RequestBody List<Long> ids) {
        R<List<ReportVo>> reportList = reportService.selectReport(ids);
        return reportList;
    }

    @ApiOperation("保存图片：病虫害")
    @PostMapping("/saveBcImg")
    public R<String> saveBcImg(@RequestParam MultipartFile file) {
        return reportService.saveBcImg(file);
    }

    @ApiOperation("保存图片：病虫害")
    @PostMapping("/saveFertWaterImg")
    public R<String> saveFertWaterImg(@RequestParam MultipartFile file) {
        return reportService.saveFertWaterImg(file);
    }

    @ApiOperation("保存图片：病虫害")
    @PostMapping("/saveRipeImg")
    public R<String> saveRipeImg(@RequestParam MultipartFile file) {
        return reportService.saveRipeImg(file);
    }

    @ApiOperation("保存图片：病虫害")
    @PostMapping("/saveWeedsImg")
    public R<String> saveWeedsImg(@RequestParam MultipartFile file) {
        return reportService.saveWeedsImg(file);
    }

}
