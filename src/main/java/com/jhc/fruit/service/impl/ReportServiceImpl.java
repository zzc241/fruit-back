package com.jhc.fruit.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.jhc.fruit.common.R;
import com.jhc.fruit.dao.ReportDao;
import com.jhc.fruit.entity.Report;
import com.jhc.fruit.service.FileService;
import com.jhc.fruit.service.ReportService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestParam;
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
    public R<Long> add(Report report,
                       @RequestParam List<MultipartFile> bcImgs,
                       @RequestParam List<MultipartFile> fertWaterImgs,
                       @RequestParam List<MultipartFile> ripeImgs,
                       @RequestParam List<MultipartFile> weedsImgs) {
        //新增
        Long id = (long) reportDao.insert(report);
        try {
            //病虫害图片
            if (bcImgs != null) {
                for (MultipartFile bcImg : bcImgs) {
                    String url = fileService.save(bcImg, "bc");
                    reportDao.saveImg(url, id, 2);
                }
            }
            //水肥图片
            if (bcImgs != null) {
                for (MultipartFile fertWaterImg : fertWaterImgs) {
                    String url = fileService.save(fertWaterImg, "fertWater");
                    reportDao.saveImg(url, id, 1);
                }
            }
            //果实成熟度图片
            if (bcImgs != null) {
                for (MultipartFile ripeImg : ripeImgs) {
                    String url = fileService.save(ripeImg, "ripe");
                    reportDao.saveImg(url, id, 4);
                }
            }
            //杂草图片
            if (bcImgs != null) {
                for (MultipartFile weedsImg : weedsImgs) {
                    String url = fileService.save(weedsImg, "weeds");
                    reportDao.saveImg(url, id, 3);
                }
            }
        }catch (Exception e){
            log.error(e.getMessage());
            if (reportDao.selectById(id) != null){
                reportDao.deleteById(id);
                reportDao.deleteImgs(id);
            }
            return R.error("上报失败");
        }
        return R.success(id);
    }
}
