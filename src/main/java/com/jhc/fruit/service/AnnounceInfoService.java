package com.jhc.fruit.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.jhc.fruit.entity.AnnounceInfo;
import com.jhc.fruit.entity.vo.AnnounceInfoVo;

import java.util.List;

/**
 * @author ：lcy
 * @description：TODO
 * @date ：2022/5/30 0030 10:04
 */
/**
 * @author ：zzc
 * @description：getNoFinish和getFinish方法
 * 改成getPageInfo和getInfo方法
 * @date ：2026/1/23
 */
public interface AnnounceInfoService extends IService<AnnounceInfo> {
    public Page<AnnounceInfoVo> getPageInfo(AnnounceInfoVo pickInfoVo);
    public List<AnnounceInfoVo> getInfo(AnnounceInfoVo pickInfoVo);
    public String changeInfo(AnnounceInfoVo pickInfoVo);
    public String updateReadInfo(Long id , Long isRead);
    public String updateFinishInfo(Long id , Long isFinish);
    public String saveDeployInfo(AnnounceInfoVo pickInfoVo);
}
