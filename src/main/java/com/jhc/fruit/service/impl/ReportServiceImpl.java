package com.jhc.fruit.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.jhc.fruit.dao.ReportDao;
import com.jhc.fruit.entity.Report;
import com.jhc.fruit.service.FileService;
import com.jhc.fruit.service.ReportService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Slf4j
@Service
public class ReportServiceImpl extends ServiceImpl<ReportDao, Report> implements ReportService {
    @Autowired
    private ReportDao reportDao;
    @Autowired
    private FileService fileService;

    @Override
    public Long add(Report report) {
        Long id = (long) reportDao.insert(report);
        return id;
    }

    @Override
    public void update(Report report) {
        LambdaQueryWrapper<Report> queryWrapper = new LambdaQueryWrapper<Report>()
                .eq(Report::getId, report.getId());
        reportDao.update(report, queryWrapper);
    }

    @Override
    public Long add(Report report, List<MultipartFile> imgs) {
        //新增
        Long id = (long) reportDao.insert(report);
        for (MultipartFile file : imgs) {
            String url = fileService.save(file, "report");
            reportDao.saveImg(url, id);
        }
        return id;
    }
}
