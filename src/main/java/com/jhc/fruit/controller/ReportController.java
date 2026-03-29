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

    @ApiOperation("通过userId查询报告")
    @GetMapping("/selectByUserId/{userId}")
    public R<List<ReportVo>> selectByUserId(@PathVariable Long userId) {
        List<ReportVo> reportList = reportService.selectByUserId(userId);
        return R.success(reportList);
    }

    @ApiOperation("根据果实id查询报告")
    @GetMapping("/get/{treeId}")
    public R<List<ReportVo>> getByTree(@PathVariable Long treeId) {
        return reportService.getByTree(treeId);
    }

    @ApiOperation("任务进度报告")
    @PostMapping("/task")
    public R<String> reportTask(@RequestParam ReportVo reportVo) {
        return reportService.reportTask(reportVo);
    }

    @ApiOperation("查询上次报告")
    @GetMapping("/getOwnTaskReport/{empId}/{treeId}/{typeId}")
    public R<ReportVo> getOwnTaskReport(@PathVariable Long empId, @PathVariable Long treeId, @PathVariable Long typeId) {
        return reportService.getOwnTaskReport(empId, treeId, typeId);
    }


    @ApiOperation("查询果园内任务报告")
    @GetMapping("/getOrchardReport/{orchardId}")
    public R<List<ReportVo>> getOrchardReport(@PathVariable Long orchardId) {
        return reportService.getOrchardReport(orchardId);
    }

    @ApiOperation("查询任务报告")
    @GetMapping("/getTaskReport/{taskId}")
    public R<ReportVo> getTaskReport(@RequestParam Long taskId) {
        return reportService.getTaskReport(taskId);
    }
}
