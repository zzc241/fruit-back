package com.jhc.fruit.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.jhc.fruit.entity.Report;
import com.jhc.fruit.entity.Task;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Mapper
public interface ReportDao extends BaseMapper<Report> {
    void saveImg(String url, Long id, Integer type);

    void deleteImgs(Long id);

    List<String> selectImgs(Long reportId, Long typeId);

    void deleteUrl(String url);


    void addUrl(String url, Long id, Long typeId);

    List<String> getImgs(Long id);

    List<Report> selectByUserId(Long userId);

    Long getOrchardId(Long userId);

    Report getOwnTaskReport(Long empId, Long treeId, Long typeId);

    List<Report> getOrchardReport(Long orchardId);

    String getUserName(Long userId);
}
