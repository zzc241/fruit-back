package com.jhc.fruit.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.jhc.fruit.common.R;
import com.jhc.fruit.dao.ReportDao;
import com.jhc.fruit.entity.Report;
import com.jhc.fruit.entity.vo.ReportVo;
import com.jhc.fruit.service.FileService;
import com.jhc.fruit.service.ReportService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import java.util.ArrayList;
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
    public void update(ReportVo reportVo) {
        //情况：文本数据更新/图片更新
        //更新文本部分
        Report report = new Report();
        BeanUtils.copyProperties(reportVo, report);
        reportDao.updateById(report);
        //更新图片
        Long id = reportVo.getId();
        List<String> bcUrls = getUrlsByType(id, 2L);
        List<String> fertWaterUrls = getUrlsByType(id, 1L);
        List<String> ripeUrls = getUrlsByType(id, 3L);
        List<String> weedsUrls = getUrlsByType(id, 4L);
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

    @Override
    public R<List<ReportVo>> selectReport(List<Long> ids) {
        //查找id对应的report，判断是否存在
        //记录哪些report是不存在的
        List<Long> noExitsIds = new ArrayList<>();
        List<ReportVo> reportVos = new ArrayList<>();
        for (Long id : ids) {
            Report report = reportDao.selectById(id);
            //判断是否存在报告
            if (report == null) {
                noExitsIds.add(id);
            }else {
                ReportVo reportVo = new ReportVo();
                //获取对应图片的临时访问url
                BeanUtils.copyProperties(report, reportVos);
                List<String> bcUrls = getUrlsByType(id, 2L);
                List<String> fertWaterUrls = getUrlsByType(id, 1L);
                List<String> ripeUrls = getUrlsByType(id, 3L);
                List<String> weedsUrls = getUrlsByType(id, 4L);
                //保存url
                reportVo.setBcUrlList(bcUrls);
                reportVo.setFertWaterUrlList(fertWaterUrls);
                reportVo.setRipeUrlList(ripeUrls);
                reportVo.setWeedsUrlList(weedsUrls);
                reportVos.add(reportVo);
            }
        }
        if (!noExitsIds.isEmpty()) {
            return R.success(reportVos);
        }else {
            //存在id没有对应报告
            return R.error("存在id没有对应的报告: " +  noExitsIds.toString());
        }
    }

    public List<String> getUrlsByType(Long reportId, Long typeId){
        try{
            List<String> urls = new ArrayList<>();
            //获取相对路径
            List<String> fileNames = reportDao.selectImgs(reportId, typeId);
            //获取临时访问url
            fileNames.forEach(fileName -> {
                urls.add(fileService.getImageUrl(fileName));
            });
            return urls;
        }catch (Exception e){
            log.error(e.getMessage());
            return null;
        }
    }
}
