package com.jhc.fruit.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.jhc.fruit.common.R;
import com.jhc.fruit.dao.ReportDao;
import com.jhc.fruit.dao.TaskDao;
import com.jhc.fruit.entity.Report;
import com.jhc.fruit.entity.Task;
import com.jhc.fruit.entity.vo.ReportVo;
import com.jhc.fruit.service.FileService;
import com.jhc.fruit.service.ReportService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Slf4j
@Service
public class ReportServiceImpl extends ServiceImpl<ReportDao, Report> implements ReportService {
    @Autowired
    private ReportDao reportDao;
    @Autowired
    private FileService fileService;
    private final List<Long> typeIds = List.of(1L, 2L, 3L, 4L);
    @Autowired
    private TaskDao taskDao;

    @Override
    public void update(ReportVo reportVo) {
        //情况：文本数据更新/图片更新
        //更新文本部分
        Report report = new Report();
        BeanUtils.copyProperties(reportVo, report);
        reportDao.updateById(report);
        //更新图片
        Long id = reportVo.getId();
        //查找已有的图片
        List<String> bcUrls = getUrlsByType(id, reportVo.getTypeId());
        //更新图片，删除旧图片，增加新图片
        handleUrl(reportVo.getUrlList(), bcUrls, id, reportVo.getTypeId());
    }

    //更新图片
    private void handleUrl(List<String> news,List<String> olds, Long id, Long typeId) {
        //解析url，获取fileName
        List<String> newFileNames = new ArrayList<>();
        news.forEach(url -> {
            Map<String, Object> map = fileService.parsePresignedUrl(url);
            newFileNames.add(map.get("objectPath").toString());
        });

        List<String> oldFileNames = new ArrayList<>();
        olds.forEach(url -> {
            Map<String, Object> map = fileService.parsePresignedUrl(url);
            oldFileNames.add(map.get("objectPath").toString());
        });
        // 对比旧URL和新URL，找出待删除/待新增的URL ==========
        // 待删除URL：旧有但新的没有
        List<String> toDeleteUrls = oldFileNames.stream()
                .filter(url -> !newFileNames.contains(url) && url != null && !url.isEmpty())
                .toList();

        // 待新增URL：新的但旧有没有
        List<String> toAddUrls = newFileNames.stream()
                .filter(url -> !oldFileNames.contains(url) && url != null && !url.isEmpty())
                .toList();

        //删除旧url
        toDeleteUrls.forEach(url -> {
            reportDao.deleteUrl(url);
        });
        //增加新url
        toAddUrls.forEach(url -> {
            reportDao.addUrl(url, id, typeId);
        });
    }

    @Override
    public R<String> addImg(Long id,
                          Long typeId,
                          MultipartFile img){

        if (id == null) {
            log.error("新增报告失败：id为空");
            return R.error("报告不能为空");
        }
        if (typeId == null || !typeIds.contains(typeId)) {
            log.error("新增报告失败：类型错误 typeId={}", typeId);
        }

        try {
            if (img != null && !img.isEmpty()) {
                if (typeId.equals(1L)){
                    // 水肥图片：修复判断逻辑 + 单个文件校验
                    String url = fileService.save(img, "fertWater");
                    if (url == null || url.isEmpty()) {
                        log.error("水肥图片保存失败，URL为空，报告ID：{}", id);
                    }
                    reportDao.saveImg(url, id, 1);
                }else if (typeId.equals(2L)){
                    // 调用save并校验返回值
                    String url = fileService.save(img, "bc");
                    if (url == null || url.isEmpty()) {
                        log.error("病虫害图片保存失败，URL为空，报告ID：{}", id);
                    }
                    reportDao.saveImg(url, id, 2);
                }else if (typeId.equals(3L)){
                    String url = fileService.save(img, "weeds");
                    if (url == null || url.isEmpty()) {
                        log.error("杂草图片保存失败，URL为空，报告ID：{}", id);
                    }
                    reportDao.saveImg(url, id, 3);
                }else if (typeId.equals(4L)){
                    String url = fileService.save(img, "ripe");
                    if (url == null || url.isEmpty()) {
                        log.error("果实成熟度图片保存失败，URL为空，报告ID：{}", id);
                    }
                    reportDao.saveImg(url, id, 4);
                }else {
                    return R.error("非法类型");
                }
            }

        } catch (Exception e) {
            log.error("新增报告及图片失败，报告ID：{}，异常信息：{}", id, e.getMessage(), e);
            // 回滚：删除新增的报告 + 关联图片
            if (reportDao.selectById(id) != null) {
                reportDao.deleteById(id);
                reportDao.deleteImgs(id);
            }
            return R.error("上传失败：" + e.getMessage());
        }
        return R.success("上传成功");
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
                BeanUtils.copyProperties(report, reportVo);
                if (reportVo.getTypeId() == null || !typeIds.contains(reportVo.getTypeId())){
                    return R.error("类型非法");
                }
                List<String> urls = getUrlsByType(id, reportVo.getTypeId());
                //保存url
                reportVo.setUrlList(urls);
                reportVos.add(reportVo);
            }
        }
        //如果没有不存在的id
        if (noExitsIds.isEmpty()) {
            return R.success(reportVos);
        }else {
            //存在id没有对应报告
            return R.error("存在id没有对应的报告: " +  noExitsIds.toString());
        }
    }

    @Override
    public R<List<Long>> add(Report report) {
        //保存
        if (report == null) {
            return R.error("报告为空");
        }
        if (report.getContent() == null || report.getContent().isEmpty()) {
            return R.error("内容为空");
        }

        reportDao.insert(report);
        //新增任务
        Task task = new Task();
        task.setCreatorId(report.getUserId());
        task.setCreateTime(report.getCreateTime());
        task.setTaskType("浇水");
        task.setTaskInfo("给该区域所有果树浇水，每株5L");
        task.setOrchardId(reportDao.getOrchardId(report.getUserId()));
        task.setTaskTitle("浇水");
        task.setDeadline(LocalDateTime.now().plusDays(1));
        taskDao.insert(task);

        Long id = report.getId();
        Long typeId = report.getTypeId();
        if (id == null) {
            return R.error("上报失败");
        }
        if (!typeIds.contains(typeId) || typeId == null) {
            return R.error("上报类型错误:" + report.getTypeId());
        }
        return R.success(List.of(id, typeId));
    }

    @Override
    public R<String> removeReport(List<Long> ids) {
        List<Long> noExitsIds = new ArrayList<>();
        for (Long id : ids) {
            Report report = reportDao.selectById(id);
            if (report == null) {
                noExitsIds.add(id);
            }else {
                reportDao.deleteById(id);
                reportDao.deleteImgs(id);
                //关联删除图片
                List<String> fileNames = reportDao.getImgs(id);
                if(fileNames != null && !fileNames.isEmpty()) {
                    for (String fileName : fileNames) {
                        if(fileService.exits(fileName)){
                            fileService.remove(fileName);
                        }
                    }
                }
            }
        }
        if (noExitsIds.isEmpty()) {
            return R.success();
        }else {
            return R.error("id不存在: " + noExitsIds.toString());
        }
    }

    @Override
    public R<String> saveBcImg(MultipartFile file) {
        return R.success(fileService.save(file, "bc"));
    }

    @Override
    public R<String> saveFertWaterImg(MultipartFile file) {
        return R.success(fileService.save(file, "fertWater"));
    }

    @Override
    public R<String> saveRipeImg(MultipartFile file) {
        return R.success(fileService.save(file, "ripe"));
    }

    @Override
    public R<String> saveWeedsImg(MultipartFile file) {
        return R.success(fileService.save(file, "weeds"));
    }


    public List<String> getUrlsByType(Long reportId, Long typeId){
        try{
            List<String> urls = new ArrayList<>();
            //获取相对路径
            List<String> fileNames = reportDao.selectImgs(reportId, typeId);
            //获取临时访问url
            //判断文件是否存在
            for (String fileName : fileNames) {
                if (fileService.exits(fileName)){
                    urls.add(fileService.getImageUrl(fileName));
                }else {
                    urls.add("文件不存在");
                }
            }
            return urls;
        }catch (Exception e){
            log.error(e.getMessage());
            return null;
        }
    }

    public List<ReportVo> selectByUserId(Long userId) { 
        List<Report> reports = reportDao.selectByUserId(userId);

        List<ReportVo> reportVos = new ArrayList<>();
        for (Report report : reports) { 
            ReportVo reportVo = new ReportVo();
            Long id = report.getId();
            //获取对应图片的临时访问url
            BeanUtils.copyProperties(report, reportVo);

            List<String> urls = getUrlsByType(id, reportVo.getTypeId());
            //保存url
            reportVo.setUrlList(urls);
            reportVos.add(reportVo);
        }

        return reportVos;
    }

    @Override
    public R<List<ReportVo>> getByTree(Long treeId) {
        LambdaQueryWrapper<Report> queryWrapper = new LambdaQueryWrapper<Report>();
        queryWrapper.eq(Report::getTreeId, treeId);
        List<Report> reports = reportDao.selectList(queryWrapper);
        List<ReportVo> reportVos = new ArrayList<>();
        for (Report report : reports) {
            ReportVo reportVo = new ReportVo();
            BeanUtils.copyProperties(report, reportVo);
            List<String> urls = getUrlsByType(report.getId(), reportVo.getTypeId());
            reportVo.setUrlList(fileService.getImageUrls(urls));
            reportVos.add(reportVo);
        }
        return R.success(reportVos);
    }

    @Override
    public R<String> reportTask(ReportVo reportVo) {
        //上报任务状态 -》任务文本汇报和任务完成图片
        Report report = new Report();
        BeanUtils.copyProperties(reportVo, report);
        //保存图片信息
        for (String url : reportVo.getUrlList()) {
            reportDao.saveImg(url, reportVo.getId(), 6);
        }
        reportDao.insert(report);
        //修改任务状态为待审核0
        Task task = taskDao.selectById(reportVo.getTaskId());
        task.setStatus(0);
        //绑定报告
        task.setReportId(reportVo.getId());
        taskDao.updateById(task);
        return R.success();
    }
}
