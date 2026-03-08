package com.jhc.fruit.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.jhc.fruit.entity.Report;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.web.multipart.MultipartFile;

@Mapper
public interface ReportDao extends BaseMapper<Report> {
    void saveImg(String url, Long id, Integer type);

    void deleteImgs(Long id);
}
