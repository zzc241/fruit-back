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
 * 改成getPageInfo和getInfo方法
 * @date ：2026/1/23
 */
public interface PickInfoService extends IService<PickInfo> {
    public Page<PickInfo> getNoFinish(int page, int pageSize, String title, Long empId);
    public Page<PickInfo> getFinish(int page, int pageSize, String title, Long empId);



    public Page<PickInfoVo> getPageInfo(PickInfoVo pickInfoVo);
    public List<PickInfoVo> getInfo(PickInfoVo pickInfoVo);
    public String changeInfo(PickInfoVo pickInfoVo);
    public String updateReadInfo(Long id);
    public String updateFinishInfo(Long id);
    public String saveDeployInfo(PickInfoVo pickInfoVo);
}
