package com.jhc.fruit.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.jhc.fruit.common.R;
import com.jhc.fruit.entity.Report;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface ReportService extends IService<Report> {
    Long add(Report report);

    void update(Report report);

    Long add(Report report, List<MultipartFile> imgs);
}
