package com.jhc.fruit.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.jhc.fruit.common.R;
import com.jhc.fruit.entity.Report;
import com.jhc.fruit.entity.vo.ReportVo;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface ReportService extends IService<Report> {

    void update(ReportVo reportVo);

    R<String> addImg(Long id, Long typeId, MultipartFile img);

    R<List<ReportVo>> selectReport(List<Long> ids);

    R<List<Long>> add(Report report);

    R<String> removeReport(List<Long> ids);

    R<String> saveBcImg(MultipartFile file);

    R<String> saveFertWaterImg(MultipartFile file);

    R<String> saveRipeImg(MultipartFile file);

    R<String> saveWeedsImg(MultipartFile file);

    List<ReportVo> selectByUserId(Long userId);

    R<List<ReportVo>> getByTree(Long treeId);

    R<String> reportTask(ReportVo reportVo);

    R<ReportVo> getOwnTaskReport(Long empId, Long treeId, Long typeId);
}
