package com.jhc.fruit.controller;
import java.util.List;

import com.jhc.fruit.entity.OrchardPoint;
import com.jhc.fruit.entity.vo.OrchardPointVo;
import com.jhc.fruit.service.OrchardService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.jhc.fruit.common.BaseContext;
import com.jhc.fruit.common.R;
import com.jhc.fruit.entity.vo.OrchardVo;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

/**
 * @author ：zzc
 * @description：果园管理接口
 * @date ：2026/1/26
 */
@RestController
@RequestMapping("/orchard")
@Api(tags = "果园管理接口")
public class OrchardController {
    @Autowired
    private OrchardService orchardService;

    @PostMapping("/list")
    @ApiOperation("后台：查看全部果园")
    public R<Page<OrchardVo>> getInfo(@RequestBody OrchardVo orchardVo){
        Page<OrchardVo> pageInfo = orchardService.getPageInfo(orchardVo);
        return R.success(pageInfo);
    }

    @PostMapping("/employee/my-orchard")
    @ApiOperation("果园管理员-查询我的果园")
    public R<List<OrchardVo>> myList(@RequestBody OrchardVo orchardVo) {
        Long currentEmpId = BaseContext.getId(); // 从登录信息获取
        if(currentEmpId == null) {
            return R.error("未获取到当前登录用户信息");
        }
        orchardVo.setEmpId(currentEmpId);
        // 固定查询当前员工的通知
        List<OrchardVo> result = orchardService.getInfo(orchardVo);
        return R.success(result);
    }

    @PostMapping("/add")
    @ApiOperation("添加果园")
    public R<Long> deployInfo(@RequestBody OrchardVo orchardVo){
        Long id = orchardService.saveOrchardInfo(orchardVo);
        return R.success(id);
    }

    @PostMapping("/change")
    @ApiOperation("修改果园内容方法")
    public R<String> changeInfo(@RequestBody OrchardVo orchardVo) {
        // 校验主键和核心参数
        if (orchardVo.getId() <= 0) {
            return R.error("果园ID必须为正数");
        }
        try {
            String result = orchardService.changeInfo(orchardVo);
            return R.success(result);
        } catch (Exception e) {
            e.printStackTrace();
            return R.error("修改果园异常：" + e.getMessage());
        }
    }
    @DeleteMapping("/delete/{id}")
    @ApiOperation("删除果园")
    public R<Boolean> deleteOrchard(@PathVariable Long id){
        Boolean result = orchardService.removeOrchard(id);
        return R.success(result);
    }

//    @PostMapping("/update")
//    private R<String> updateOrchard(@RequestBody OrchardVo orchardVo){
//        return orchardServiceImpl.updateByOrchardVo(orchardVo);
//    }

    @PostMapping("/addPoints")
    @ApiOperation("添加果园坐标")
    public R<String> addPoint(@RequestBody OrchardPointVo orchardPointVo){
        return orchardService.addPoint(orchardPointVo);
    }

    @GetMapping("/getPoints/{orchardId}")
    @ApiOperation("获取果园坐标")
    public R<List<List<Double>>> getPoints(@PathVariable Long orchardId){
        return orchardService.getPoints(orchardId);
    }
}
