package com.jhc.fruit.controller;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.jhc.fruit.common.BaseContext;
import com.jhc.fruit.common.R;
import com.jhc.fruit.entity.PickInfo;
import com.jhc.fruit.entity.vo.PickInfoVo;
import com.jhc.fruit.service.PickInfoService;
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
 */
@RestController
@RequestMapping("/pickInfo")
@Api(tags = "消息通知接口")
public class PickInfoController {
    @Autowired
    private PickInfoService pickInfoService;


    //这两个留下来主要是测试的时候用的，正式环境下用/list接口
    @GetMapping("/getFinish")
    @ApiOperation("后台：查看全部完成通知")
    public R<Page<PickInfo>> getFinishInfo(
        @RequestParam(value = "page", defaultValue = "1") Integer page,
        @RequestParam(value = "pageSize", defaultValue = "10") Integer pageSize,
        @RequestParam(required = false) String title,
        @RequestParam(required = false) Long empId){
        Page<PickInfo> pageInfo = pickInfoService.getFinish(page, pageSize, title, empId);
        return R.success(pageInfo);
    }

    @GetMapping("/getNoFinish")
    @ApiOperation("后台：查看全部未完成通知")
    public R<Page<PickInfo>> getNoFinishInfo(
        @RequestParam(value = "page", defaultValue = "1") Integer page,
        @RequestParam(value = "pageSize", defaultValue = "10") Integer pageSize,
        @RequestParam(required = false) String title,
        @RequestParam(required = false) Long empId){
        //进行分页查询
        Page<PickInfo> pageInfo = pickInfoService.getNoFinish(page, pageSize, title, empId);
        return R.success(pageInfo);
    }





    @PostMapping("/list")
    @ApiOperation("后台：查看全部通知")
    public R<Page<PickInfo>> getInfo(@RequestBody PickInfoVo pickInfoVo){
        Page<PickInfo> pageInfo = pickInfoService.getPageInfo(pickInfoVo);
        return R.success(pageInfo);
    }
    //其中getPickInfoVo类中只需要传title，isFinish，isRead三个参数，empId从登录信息获取
    @PostMapping("/employee/my-list")
    @ApiOperation("员工-查询我的通知")
    public R<List<PickInfo>> myList(@RequestBody PickInfoVo pickInfoVo) {
        Long currentEmpId = BaseContext.getId(); // 从登录信息获取
        if(currentEmpId == null) {
            return R.error("未获取到当前登录用户信息");
        }
        // 固定查询当前员工的通知
        List<PickInfo> result = pickInfoService.getInfo(
            pickInfoVo.getTitle(), 
            currentEmpId, 
            pickInfoVo.getIsFinish(), 
            pickInfoVo.getIsRead()
        );
        
        return R.success(result);
    }


    @PostMapping("/publish")
    @ApiOperation("发布通知")
    public R<Boolean> deployInfo(@RequestBody PickInfoVo pickInfoVo){
        Boolean result = pickInfoService.saveDeployInfo(pickInfoVo);
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
    public R<Boolean> readInfo(@PathVariable Long id){
        Boolean result = pickInfoService.updateReadInfo(id);
        return R.success(result);
    }
    @PutMapping("/finish/{id}")
    @ApiOperation("完成通知方法")
    @ApiImplicitParam(name = "id",value = "通知对应的id")
    public R<Boolean> finishInfo(@PathVariable Long id){
        Boolean result = pickInfoService.updateFinishInfo(id);
        return R.success(result);
    }
    @PostMapping("/change")
    @ApiOperation("修改通知内容方法")
    public R<Boolean> changeInfo(@RequestBody PickInfoVo pickInfoVo) {
        // 校验主键和核心参数
        if (pickInfoVo.getId() == null || pickInfoVo.getId() <= 0) {
            return R.error("通知ID不能为空且必须为正数");
        }
        try {
            Boolean result = pickInfoService.changeInfo(pickInfoVo);
            return result ? R.success(true) : R.error("修改通知失败，通知ID不存在");
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
            boolean deleteSuccess = pickInfoService.removeById(id);
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
