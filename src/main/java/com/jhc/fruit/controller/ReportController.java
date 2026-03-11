package com.jhc.fruit.controller;

import com.jhc.fruit.common.R;
import com.jhc.fruit.entity.Report;
import com.jhc.fruit.entity.vo.ReportVo;
import com.jhc.fruit.service.ReportService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
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

    @ApiOperation("新增(图片文件)")
    @PostMapping("/addImg/{reportId}/{typeId}")
    public R<String> addImg(
            @PathVariable Long reportId,
            @PathVariable Long typeId,
            @RequestParam MultipartFile img
    ) {
        return reportService.addImg(reportId, typeId, img);
    }

    @ApiOperation("新增")
    @PostMapping( "/add")
    public R<List<Long>> add(
            @RequestBody Report report) {
        return reportService.add(report);
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

    @ApiOperation("保存图片：水肥")
    @PostMapping("/saveFertWaterImg")
    public R<String> saveFertWaterImg(@RequestParam MultipartFile file) {
        return reportService.saveFertWaterImg(file);
    }

    @ApiOperation("保存图片：成熟度")
    @PostMapping("/saveRipeImg")
    public R<String> saveRipeImg(@RequestParam MultipartFile file) {
        return reportService.saveRipeImg(file);
    }

    @ApiOperation("保存图片：杂草")
    @PostMapping("/saveWeedsImg")
    public R<String> saveWeedsImg(@RequestParam MultipartFile file) {
        return reportService.saveWeedsImg(file);
    }

}
