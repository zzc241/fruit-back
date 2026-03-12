package com.jhc.fruit.controller;

import com.jhc.fruit.entity.Area;
import com.jhc.fruit.entity.vo.DisasterTypeVo;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import com.jhc.fruit.common.R;
import com.jhc.fruit.entity.Disaster;
import com.jhc.fruit.entity.vo.DisasterVo;
import com.jhc.fruit.service.DisasterService;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;

import java.util.List;
import java.util.Map;


@Slf4j
@RequestMapping("/disaster")
@RestController
@Api(tags = "灾害管理接口")
public class DisasterController {
    @Autowired
    private DisasterService disasterService;
    //添加灾害接口
    @ApiOperation(value = "添加灾害接口")
    @PostMapping("/add")
    public R<String> add(@RequestBody DisasterVo disasterVo){
        log.info("添加灾害1：{}", disasterVo);
        Disaster disaster = new Disaster();
        //将disasterVo中的属性值复制到disaster中
        BeanUtils.copyProperties(disasterVo, disaster);
        log.info("添加灾害：{}", disaster);
        //调用service层的save方法将disaster保存到数据库中
        boolean saveResult = disasterService.save(disaster);
        return R.success("添加灾害成功");
    }
    //删除灾害接口
    @ApiOperation(value = "删除灾害接口")
    @DeleteMapping("/delete")
    public R<String> delete(@RequestBody Long id){
        boolean deleteResult = disasterService.removeById(id);
        return R.success("删除灾害成功");
    }
    //修改灾害接口
    @ApiOperation(value = "修改灾害接口")
    @PostMapping("/update")
    public R<String> update(@RequestBody DisasterVo disasterVo){
        Disaster disaster = new Disaster();
        BeanUtils.copyProperties(disasterVo, disaster);
        boolean updateResult = disasterService.updateById(disaster);
        return R.success("修改灾害成功");
    }
    //查询灾害接口
    @ApiOperation(value = "查询灾害接口")
    @GetMapping("/get")
    public R<DisasterVo> get(@RequestParam Long OrchardId){
        DisasterVo getResult = disasterService.getByOrchardId(OrchardId);
        DisasterVo resultVo = new DisasterVo();
        BeanUtils.copyProperties(getResult, resultVo);
        return R.success(resultVo);
    }

    @ApiOperation(value = "统计病虫害种类及其出现次数(果园）")
    @GetMapping("/statisticsDisaster")
    public R<List<DisasterTypeVo>> statisticsDisaster(@PathVariable Long orchardId){
        return disasterService.statisticsDisaster(orchardId);
    }

    @ApiOperation(value = "统计病虫害种类及其出现次数(区域）")
    @GetMapping("/statisticsAreaDisaster")
    public R<List<Map<Area,List<DisasterTypeVo>>>> statisticsAreaDisaster(@PathVariable Long orchardId){
        return disasterService.statisticsAreaDisaster(orchardId);
    }
}
