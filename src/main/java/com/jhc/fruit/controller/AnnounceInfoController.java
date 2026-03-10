package com.jhc.fruit.controller;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.jhc.fruit.common.BaseContext;
import com.jhc.fruit.common.R;
import com.jhc.fruit.entity.vo.AnnounceInfoVo;
import com.jhc.fruit.service.AnnounceInfoService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * @author ：lcy
 * @description：TODO
 * @date ：2022/5/30 0030 9:56
 * 
 * @author : zzc
 * @description : 其中/getFinish和/getNoFinish已经合并到/list接口中，通过传参isFinish来区分
 * 修改发布通知方法，添加修改通知方法
 * @date : 2026/1/23
 * @description : 完善添加通知方法和修改通知方法的参数校验
 * @date : 2026/1/27
 * @description : pickInfo改成announceInfo，之前的接口用不了删除了
 * @date : 2026/1/28
 */
@RestController
@RequestMapping("/announceInfo")
@Api(tags = "消息通知接口")
public class AnnounceInfoController {
    @Autowired
    private AnnounceInfoService announceInfoService;

    @PostMapping("/listPage")
    @ApiOperation("后台：查看全部通知分页查询")
    public R<Page<AnnounceInfoVo>> getPageInfo(@RequestBody AnnounceInfoVo pickInfoVo){
        Page<AnnounceInfoVo> pageInfo = announceInfoService.getPageInfo(pickInfoVo);
        return R.success(pageInfo);
    }
    @PostMapping("/list")
    @ApiOperation("后台：查看全部通知")
    public R<List<AnnounceInfoVo>> getInfo(@RequestBody AnnounceInfoVo pickInfoVo){
        List<AnnounceInfoVo> info = announceInfoService.getInfo(pickInfoVo);
        return R.success(info);
    }
    //其中getPickInfoVo类中只需要传title，isFinish，isRead三个参数，empId从登录信息获取
    @PostMapping("/employee/my-list")
    @ApiOperation("员工-查询我的通知")
    public R<List<AnnounceInfoVo>> myList(@RequestBody AnnounceInfoVo pickInfoVo) {
        Long currentEmpId = BaseContext.getId(); // 从登录信息获取
        if(currentEmpId == null) {
            return R.error("未获取到当前登录用户信息");
        }
        //将登录id放到pickInfoVo中
        pickInfoVo.setEmpId(currentEmpId);
        // 固定查询当前员工的通知
        List<AnnounceInfoVo> result = announceInfoService.getInfo(pickInfoVo);
        return R.success(result);
    }


    @PostMapping("/publish")
    @ApiOperation("发布通知")
    public R<String> deployInfo(@RequestBody AnnounceInfoVo pickInfoVo){
        String result = announceInfoService.saveDeployInfo(pickInfoVo);
        return R.success(result);
    }

    /**
     * 已读通知方法
     * @param id
     * @return
     */
    @PutMapping("/read/{id}")
    @ApiOperation("已读通知方法")
    @ApiImplicitParam(name = "id",value = "通知对应的id")
    public R<String> readInfo(@PathVariable Long id , @RequestParam Long isRead){
        String result = announceInfoService.updateReadInfo(id , isRead);
        return R.success(result);
    }
    @PutMapping("/finish/{id}")
    @ApiOperation("完成通知方法")
    @ApiImplicitParam(name = "id",value = "通知对应的id")
    public R<String> finishInfo(@PathVariable Long id , @RequestParam Long isFinish){
        String result = announceInfoService.updateFinishInfo(id , isFinish);
        return R.success(result);
    }
    @PostMapping("/change")
    @ApiOperation("修改通知内容方法")
    public R<String> changeInfo(@RequestBody AnnounceInfoVo pickInfoVo) {
        // 校验主键和核心参数
        if (pickInfoVo.getId() == null || pickInfoVo.getId() <= 0) {
            return R.error("通知ID不能为空且必须为正数");
        }
        try {
            String result = announceInfoService.changeInfo(pickInfoVo);
            return R.success(result);
        } catch (Exception e) {
            e.printStackTrace();
            return R.error("修改通知异常：" + e.getMessage());
        }
    }
    @DeleteMapping("/{id}")
    @ApiOperation("删除通知方法")
    @ApiImplicitParam(name = "id", value = "通知对应的id", required = true)
    public R<String> deleteInfo(@PathVariable Long id) {
        try {
            boolean deleteSuccess = announceInfoService.removeById(id);
            if (deleteSuccess) {
                return R.success("删除成功");
            } else {
                return R.error("删除失败，通知ID不存在");
            }
        } catch (Exception e) {
            e.printStackTrace();
            return R.error("删除通知异常：" + e.getMessage());
        }
    }

}
