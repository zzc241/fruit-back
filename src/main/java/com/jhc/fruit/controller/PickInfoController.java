package com.jhc.fruit.controller;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.jhc.fruit.common.R;
import com.jhc.fruit.entity.PickInfo;
import com.jhc.fruit.service.PickInfoService;
import com.jhc.fruit.service.impl.PickInfoServiceImpl;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * @author ：lcy
 * @description：TODO
 * @date ：2022/5/30 0030 9:56
 */
@RestController
@RequestMapping("/pickInfo")
@Api(tags = "消息通知接口")
public class PickInfoController {
    @Autowired
    private PickInfoServiceImpl pickInfoServiceImpl;

    @Autowired
    private PickInfoService pickInfoService;

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


    @PostMapping
    @ApiImplicitParams({
            @ApiImplicitParam( name = "title",value = "通知标题[放假通知、任务通知....]"),
            @ApiImplicitParam( name = "content",value = "通知内容"),
            @ApiImplicitParam( name = "empId",value = "发送通知的员工对应id，如全体员工则不填[id会标记为1]")
    })
    public R<String> deployInfo(@RequestBody PickInfo pickInfo){
        pickInfoServiceImpl.save(pickInfo);
        return R.success("发布成功");
    }

    @GetMapping("/{id}")
    @ApiOperation("查看自己收到的通知方法")
    @ApiImplicitParam(name = "id",value = "员工id")
    public R<List<PickInfo>> getInfo(@PathVariable Long id){
        List<PickInfo> infos = pickInfoServiceImpl.getMine(id);
        return R.success(infos);
    }

    /**
     * 已读通知方法
     * @param id
     * @return
     */
    @PutMapping("/{id}")
    @ApiOperation("已读通知方法")
    @ApiImplicitParam(name = "id",value = "通知对应的id")
    public R<PickInfo> readInfo(@PathVariable Long id){
        PickInfo info = pickInfoServiceImpl.getById(id);
        info.setIsRead(true);
        pickInfoServiceImpl.updateById(info);
        return R.success(info);
    }

    @DeleteMapping("/{id}")
    @ApiOperation("删除通知方法")
    @ApiImplicitParam(name = "id",value = "通知对应的id")
    public R<String> deleteInfo(@PathVariable Long id){
        pickInfoServiceImpl.removeById(id);
        return R.success("删除成功");
    }

//    @GetMapping
//    @ApiOperation("后台：查看全部通知")
//    public R<Page> getAll(int page,int pageSize,String title,Long empId){
//        //创建分页构造器
//        Page fpageInfo = new Page(page,pageSize);
//        Page pageInfo = new Page(page,pageSize);
//        if(title != null){
//            title += "%";
//        }
//        //进行分页查询
//        pageInfo.setRecords(pickInfoServiceImpl.getAll(pageInfo, empId, title));

//        System.out.println("通知的全部的pageInfo的getRecords：" + pageInfo.getRecords());
//        //对分页数据进行处理
//        List<PickInfo> records = pageInfo.getRecords();

//        List<List<PickInfo>> pickInfoRecords = new ArrayList<>();
//        List<PickInfo> finishRecords = new ArrayList<>();
//        List<PickInfo> nfinishRecords = new ArrayList<>();

//        for(PickInfo pickInfo : records){
//            //判断采摘通知信息是否完成
//            if(pickInfo.getIsFinish()){
//                finishRecords.add(pickInfo);
//            } else{
//                nfinishRecords.add(pickInfo);
//            }
//        }
//        //将分好类的通知列表添加到总通知中
//        pickInfoRecords.add(finishRecords);
//        pickInfoRecords.add(nfinishRecords);
//        //设置回分页类中
//        pageInfo.setRecords(pickInfoRecords);
//        return R.success(pageInfo);
//    }

}
