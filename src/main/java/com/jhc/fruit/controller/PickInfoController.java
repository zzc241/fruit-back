package com.jhc.fruit.controller;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.jhc.fruit.common.BaseContext;
import com.jhc.fruit.common.R;
import com.jhc.fruit.entity.PickInfo;
import com.jhc.fruit.entity.vo.PickInfoVo;
import com.jhc.fruit.entity.vo.getPickInfoVo;
import com.jhc.fruit.service.PickInfoService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiOperation;

import org.springframework.beans.BeanUtils;
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
    public R<Page<PickInfo>> getInfo(
        @RequestBody getPickInfoVo getPickInfoVo){
        Page<PickInfo> pageInfo = pickInfoService.getPageInfo(getPickInfoVo);
        return R.success(pageInfo);
    }
    //其中getPickInfoVo类中只需要传title，isFinish，isRead三个参数，empId从登录信息获取
    @PostMapping("/employee/my-list")
    @ApiOperation("员工-查询我的通知")
    public R<List<PickInfo>> myList(@RequestBody getPickInfoVo getPickInfoVo) {
        Long currentEmpId = BaseContext.getId(); // 从登录信息获取
        if(currentEmpId == null) {
            return R.error("未获取到当前登录用户信息");
        }
        // 固定查询当前员工的通知
        List<PickInfo> result = pickInfoService.getInfo(
            getPickInfoVo.getTitle(), 
            currentEmpId, 
            getPickInfoVo.getIsFinish(), 
            getPickInfoVo.getIsRead()
        );
        
        return R.success(result);
    }


    @PostMapping("/publish")
    @ApiOperation("发布通知")
    public R<Boolean> deployInfo(@RequestBody PickInfoVo pickInfoVo){
        PickInfo pickInfo = new PickInfo();
        BeanUtils.copyProperties(pickInfoVo, pickInfo);
        Boolean result = pickInfoService.save(pickInfo);
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
    public R<PickInfo> readInfo(@PathVariable Long id){
        PickInfo info = pickInfoService.getById(id);
        info.setIsRead(true);
        pickInfoService.updateById(info);
        return R.success(info);
    }
    @PostMapping("/change/{id}")
    @ApiOperation("修改通知内容方法")
    public R<PickInfo> changeInfo(@RequestBody PickInfoVo pickInfoVo){
        PickInfo pickInfo = new PickInfo();
        BeanUtils.copyProperties(pickInfoVo, pickInfo);
        pickInfoService.updateById(pickInfo);
        return R.success(pickInfo);
    }

    @DeleteMapping("/{id}")
    @ApiOperation("删除通知方法")
    @ApiImplicitParam(name = "id",value = "通知对应的id")
    public R<String> deleteInfo(@PathVariable Long id){
        pickInfoService.removeById(id);
        return R.success("删除成功");
    }

}
