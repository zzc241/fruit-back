package com.jhc.fruit.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.jhc.fruit.common.R;
import com.jhc.fruit.entity.Report;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface ReportService extends IService<Report> {
    Long add(Report report);

    void update(Report report);

    R<Long> add(Report report, @RequestParam List<MultipartFile> bcImgs,
             @RequestParam List<MultipartFile> fertWaterImgs,
             @RequestParam List<MultipartFile> ripeImgs,
             @RequestParam List<MultipartFile> zcImgs);
}
