package com.jhc.fruit.controller;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.jhc.fruit.common.BaseContext;
import com.jhc.fruit.common.R;
import com.jhc.fruit.entity.vo.OrchardVo;
import com.jhc.fruit.service.impl.OrchardServiceImpl;

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
    private OrchardServiceImpl orchardServiceImpl;
    @PostMapping("/list")
    @ApiOperation("后台：查看全部果园")
    public R<Page<OrchardVo>> getInfo(@RequestBody OrchardVo orchardVo){
        Page<OrchardVo> pageInfo = orchardServiceImpl.getPageInfo(orchardVo);
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
        List<OrchardVo> result = orchardServiceImpl.getInfo(orchardVo);
        return R.success(result);
    }

    @PostMapping("/add")
    @ApiOperation("添加果园")
    public R<String> deployInfo(@RequestBody OrchardVo orchardVo){
        String result = orchardServiceImpl.saveOrchardInfo(orchardVo);
        return R.success(result);
    }

    @PostMapping("/change")
    @ApiOperation("修改果园内容方法")
    public R<String> changeInfo(@RequestBody OrchardVo orchardVo) {
        // 校验主键和核心参数
        if (orchardVo.getId() <= 0) {
            return R.error("果园ID必须为正数");
        }
        try {
            String result = orchardServiceImpl.changeInfo(orchardVo);
            return R.success(result);
        } catch (Exception e) {
            e.printStackTrace();
            return R.error("修改果园异常：" + e.getMessage());
        }
    }
    @DeleteMapping("/delete/{id}")
    @ApiOperation("删除果园")
    public R<Boolean> deleteOrchard(@PathVariable Long id){
        Boolean result = orchardServiceImpl.removeById(id);
        return R.success(result);
    }
}
