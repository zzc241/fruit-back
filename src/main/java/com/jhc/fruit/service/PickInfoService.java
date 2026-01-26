package com.jhc.fruit.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.jhc.fruit.entity.PickInfo;
import com.jhc.fruit.entity.vo.PickInfoVo;

import java.util.List;

/**
 * @author ：lcy
 * @description：TODO
 * @date ：2022/5/30 0030 10:04
 */
/**
 * @author ：zzc
 * @description：getNoFinish和getFinish方法
 * 改成getPageInfo和getInfo方法，通过传参区分
 * @date ：2026/1/23
 */
public interface PickInfoService extends IService<PickInfo> {
    public Page<PickInfo> getNoFinish(int page, int pageSize, String title, Long empId);
    public Page<PickInfo> getFinish(int page, int pageSize, String title, Long empId);



    public Page<PickInfo> getPageInfo(PickInfoVo pickInfoVo);
    public List<PickInfo> getInfo(String title, Long empId , Boolean isFinish , Boolean isRead);
    public Boolean changeInfo(PickInfoVo pickInfoVo);
    public Boolean updateReadInfo(Long id);
    public Boolean updateFinishInfo(Long id);
    public Boolean saveDeployInfo(PickInfoVo pickInfoVo);
}
