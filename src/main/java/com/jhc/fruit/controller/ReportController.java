package com.jhc.fruit.controller;

import com.jhc.fruit.common.R;
import com.jhc.fruit.entity.Report;
import com.jhc.fruit.service.ReportService;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Slf4j
@RestController
@RequestMapping("/report")
public class ReportController {

    @Autowired
    private ReportService reportService;

    @ApiOperation("新增")
    @PostMapping("/add")
    public R<Long> add(@RequestBody Report report,
                       @RequestParam List<MultipartFile> imgs) {
        Long id = reportService.add(report, imgs);
        return R.success(id);
    }

    @ApiOperation("修改")
    @PostMapping("/update")
    public R<String> update(@RequestBody Report report) {
        reportService.update(report);
        return R.success();
    }

    @ApiOperation("删除")
    @DeleteMapping("/delete")
    public R<String> delete(@RequestBody List<Long> ids) {
        reportService.removeByIds(ids);
        return R.success();
    }

    @ApiOperation("查询单个")
    @GetMapping("/{id}")
    public R<Report> get(@PathVariable Long id) {
        return R.success(reportService.getById(id));
    }

    @ApiOperation("查询多个")
    @PostMapping("/select")
    public R<List<Report>> select(@RequestBody List<Long> ids) {
        List<Report> reportList = reportService.listByIds(ids);
        return R.success(reportList);
    }
}
