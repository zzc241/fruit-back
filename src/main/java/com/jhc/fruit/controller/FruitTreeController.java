package com.jhc.fruit.controller;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.jhc.fruit.aop.Log;
import com.jhc.fruit.common.*;
import com.jhc.fruit.config.AIConfig;
import com.jhc.fruit.entity.*;
import com.jhc.fruit.entity.vo.FruitTreeVo;
import com.jhc.fruit.entity.vo.QRTreeVo;
import com.jhc.fruit.service.FileService;
import com.jhc.fruit.service.FruitGrowService;
import com.jhc.fruit.service.FruitTreeService;
import com.jhc.fruit.service.FruitTypeService;
import com.jhc.fruit.service.impl.*;
import io.swagger.annotations.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.bind.DefaultValue;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.*;
import java.util.stream.Collectors;

/**
 * @author ：lcy
 * @description：TODO
 * @date ：2022/5/17 0017 9:46
 */
@RestController
@RequestMapping("/fruitTree")
@Api(tags = "果树管理接口")
public class FruitTreeController {
    @Autowired
    private FruitTreeService fruitTreeService;

    @Autowired
    private FruitGrowService fruitGrowService;

    @Autowired
    private FruitTypeService fruitTypeService;

    @Autowired
    private FileService fileService;



    @GetMapping
    @ApiOperation("分页查询果树方法")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "page",value = "当前页"),
            @ApiImplicitParam(name = "pageSize", value = "最大页码")
    })
    public R<Page> page(int page,int pageSize,String type){
        //模糊查询添加符号
        if(type != null){
            type += "%";
        }
        Page<FruitTreeVo> pageInfo = new Page(page,pageSize);
        Page<FruitTree> page1 = new Page(page,pageSize);
        pageInfo.setTotal(fruitTreeService.page(page1).getTotal());
        pageInfo.setPages(fruitTreeService.page(page1).getPages());
        List<FruitTreeVo> result = fruitTreeService.pageSelect(pageInfo, type);
        pageInfo.setRecords(result);

        System.out.println(pageInfo.getRecords());
        return R.success(pageInfo);
    }

    /**
     * 新增果树功能
     * @param fruitTree
     * @return
     */
    @PostMapping
    @ApiOperation("添加果树方法")
    public R<String> save(@RequestBody FruitTree fruitTree){
        //1.获取添加果树的种类，判断是否符合种在对应区块
        Long typeId = fruitTree.getType();
        //获取区号
        Long areaId = (long) fruitTree.getAreaId();
        //获取对应区块种植的蔬菜
        Long trueTypeId = fruitTreeService.getTypeId(areaId);
        //判断添加的果树与对应区块种植的果树的类型是否相同
        //如果果树类型不同
        if(!typeId.equals(trueTypeId)){
            return R.error("当前区块不能添加种植此类型作物");
        }
        //如果类型相同
        FruitTree fruitTreeSave = new FruitTree();
        BeanUtils.copyProperties(fruitTree, fruitTreeSave);
        //添加果蔬
        boolean treeSave = fruitTreeService.save(fruitTreeSave);
        if(!treeSave){
           return R.error("新增失败");
        }
        //创建新果树生长对象
        FruitGrow fruitGrow = new FruitGrow();
        fruitGrow.setTreeId(fruitTree.getId());
        fruitGrowService.save(fruitGrow);
        return R.success("新增果树成功");
    }

    /**
     * 根据id查询果树信息
     * @param id
     * @return
     */
    @GetMapping("/{id}")
    @ApiOperation("根据id查询果树信息，用于修改果树时回溯信息")
    public R<FruitTreeVo> getById(@PathVariable Long id){
        FruitTree fruitTree = fruitTreeService.getById(id);
        FruitTreeVo fruitTreeVo = new FruitTreeVo();
        BeanUtils.copyProperties(fruitTree, fruitTreeVo);
        fruitTreeVo.setType(fruitTypeService.getTypeById(fruitTree.getType()));
        
        if(fruitTreeVo != null){
            return R.success(fruitTreeVo);
        }
        return R.error("没有查询到对应果树信息");
    }

    /**
     * 根据id修改用户信息
     * @param fruitTree
     * @return
     */
    @PutMapping
    @ApiOperation("修改果蔬信息")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "type",value = "果树类型")
    })
    public R<String> update(@RequestBody FruitTree fruitTree){
        // Long empId = (Long)request.getSession().getAttribute("emplopyee");
        //其中前端页面会对数字进行四舍五入，丢失了精度，导致id不一样
        //解决：将long型数据统一转为String字符串
//        employee.setUpdateTime(LocalDateTime.now());
//        employee.setUpdateUser(empId);
        boolean b = fruitTreeService.updateById(fruitTree);
        if(!b){
            return R.error("修改失败");
        }
        return R.success("果数目信息修改成功");
    }

    /**
     * 根据id删除摄像头
     * @param id
     * @return
     */
    @DeleteMapping("/{id}")
    @ApiOperation("根据id删除果树")
    @ApiImplicitParam(name="id",value ="要删除果树的对应id")
    public R<String> delete(@PathVariable Long id){
        FruitTree fruitTree = fruitTreeService.getById(id);
        if(fruitTree == null){
            return R.error("找不到当前果树");
        }
        //删除对应图片
        QiniuUtils.deleteFileFromQiniu(fruitTree.getImg());
        FruitGrow fruitGrow = fruitGrowService.getById(id);
        if(fruitGrow != null){
/*            String firstTerm = fruitGrow.getFirstTerm();

            //删除服务器上的图片
            if(firstTerm != null){
                QiniuUtils.deleteFileFromQiniu(firstTerm);
                String secondTerm = fruitGrow.getSecondTerm();
                if(secondTerm != null){
                    QiniuUtils.deleteFileFromQiniu(secondTerm);
                    String thirdTerm = fruitGrow.getThirdTerm();
                    if(thirdTerm != null){
                        QiniuUtils.deleteFileFromQiniu(thirdTerm);
                    }
                }
            }*/
            //删除对应生长过程
            fruitGrowService.removeById(fruitGrow.getTreeId());
            fruitTreeService.removeById(id);
        }
//        fruitTreeServiceImpl.removeById(id);
        return R.success("删除成功");
    }

    @GetMapping("/showType")
    @ApiOperation("展示果树类型的数据[id + 类型]")
    public R<List<FruitType>> showType(){
        List<FruitType> fruitTypes = fruitTreeService.showType();
        return R.success(fruitTypes);
    }

    @GetMapping("/select/{areaId}")
    @ApiOperation("根据区号展示果树")
    @ApiImplicitParam(name="areaId",value ="区块id")
    public R<List<FruitTree>> selectByAreaId(@PathVariable Long areaId){
        List<FruitTree> fruitTreeList = fruitTreeService.selectByAreaIdList(areaId);
        if(fruitTreeList == null && fruitTreeList.size() == 0){
            return R.error("查找失败");
        }
        return R.success(fruitTreeList);
    }

    @GetMapping("/select/sortByCount/{areaId}")
    @ApiOperation("根据区号展示果树完后按果实数量降序")
    @ApiImplicitParam(name="areaId",value ="区块id")
    public R<List<FruitTreeVo>> sortByCount(@PathVariable Long areaId){
        List<FruitTreeVo> fruitTreeList = fruitTreeService.sortByCount(areaId);
        if(fruitTreeList == null && fruitTreeList.size() == 0){
            return R.error("查找失败");
        }
        return R.success(fruitTreeList);
    }

    @GetMapping("/select/sortByRipeCount/{areaId}")
    @ApiOperation("根据区号展示果树完后成熟果实实数量降序")
    @ApiImplicitParam(name="areaId",value ="区块id")
    public R<List<FruitTreeVo>> sortByRipeCount(@PathVariable Long areaId){
        List<FruitTreeVo> fruitTreeList = fruitTreeService.sortByRipeCount(areaId);
        if(fruitTreeList == null && fruitTreeList.size() == 0){
            return R.error("查找失败");
        }
        return R.success(fruitTreeList);
    }

    @GetMapping("/area/{areaId}")
    @ApiOperation("区域果树详情信息")
    public R<List<FruitTreeVo>> myTreeInfo(@PathVariable Long areaId){ 
        LambdaQueryWrapper<FruitTree> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(FruitTree::getAreaId, areaId);
        queryWrapper.orderByDesc(FruitTree::getCreateTime); 
        List<FruitTree> fruitTreeList = fruitTreeService.list(queryWrapper);
        if(fruitTreeList == null && fruitTreeList.size() == 0){
            return R.error("查找失败");
        }
        List<FruitTreeVo> fruitTreeVoList = new ArrayList<>();
        fruitTreeList.forEach(fruitTree -> {
            fruitTreeVoList.add(fruitToVo(fruitTree));
        });
        return R.success(fruitTreeVoList);
    }
    private FruitTreeVo fruitToVo(FruitTree fruitTree) { 
        FruitTreeVo fruitTreeVo = new FruitTreeVo();
        fruitTreeVo.setId(fruitTree.getId());
        Double ripeDegree = fruitTree.getRipeNum() * 1.0 / fruitTree.getCountNum();
        fruitTreeVo.setRipeDegree(ripeDegree);
        fruitTreeVo.setHealthCondition(fruitTree.getHealthCondition());
        return fruitTreeVo;
    }

    /**
     * 生成二维码后生成到minio上，
     * 返回图片的存储路径 MinIO服务地址/桶名称/目录/文件名.扩展名
     * @param id
     * @return R.success(QRPat
     * */
    @GetMapping("/createTreeQR/{id}")
    @ApiOperation("生成展示果树详情消息的二维码")
    @ApiImplicitParam(name = "id", value = "果树id")
    public R<String> createTreeQR(@PathVariable Long id){
        //先判断该id对应的果树是否存在
        FruitTree fruitTree = fruitTreeService.getById(id);
        if (fruitTree == null) {
            return  R.error("目标果树不存在");
        }
        //如果果树存在
        //上传二维码并上传到minio
        String QRPath = fileService.upload(QRCodeUtil.createTreeQRCode(id));
        //返回图片在minio上的路径
        return R.success(QRPath);
    }

    @PostMapping("/createTreeQRList")
    @ApiOperation("批量生成展示果树详情消息的二维码")
    @ApiImplicitParam(name = "ids", value = "果树id集合")
    public R<List<String>> createTreeQR(@RequestParam List<Long> ids){
        //先判断该id对应的果树是否存在
        List<FruitTree> fruitTrees = fruitTreeService.getByIds(ids);
        if (fruitTrees == null || fruitTrees.isEmpty()) {
            return  R.error("目标果树不存在");
        }

        //校验「传入的ID数量」和「查询到的数量」是否一致（避免部分ID不存在）
        if (fruitTrees.size() != ids.size()) {
            //找出不存在的ID
            Set<Long> existIds = fruitTrees.stream()
                    .map(FruitTree::getId)
                    .collect(Collectors.toSet());
            Set<Long> notExistIds = ids.stream()
                    .filter(id -> !existIds.contains(id))
                    .collect(Collectors.toSet());
            return R.error("部分果树不存在，不存在的ID：" + notExistIds);
        }
        //如果果树都存在
        String treeUrl = "https://127.0.0.1:3388/fruitTree/";
        List<String> urls = new ArrayList<>();
        for (FruitTree fruitTree : fruitTrees) {
            urls.add(treeUrl + fruitTree.getId());
        }
        //上传二维码并上传到minio
        List<String> QRPaths = fileService.uploads(QRCodeUtil.createQRList(urls));
        //返回图片在minio上的路径
        return R.success(QRPaths);
    }

    @PostMapping("/showTree")
    @ApiOperation("扫码查看果树信息")
    public R<FruitTreeVo> showTree(@RequestParam MultipartFile imgFile){
        return fruitTreeService.showTree(imgFile);
    }

    @GetMapping("/myTree/{id}")
    @ApiOperation("我的果树")
    public R<List<FruitTreeVo>> myTree(@PathVariable Long id){
        return fruitTreeService.myTree(id);
    }

    @GetMapping("/myTree/{userId}/{treeId}")
    @ApiOperation("我的果树详情信息")
    public R<FruitTreeVo> myTreeInfo(@PathVariable Long userId, @PathVariable Long treeId){
        return fruitTreeService.myTreeInfo(userId, treeId);
    }

    private AIConfig aiConfig;

    @ApiOperation("AI识别果实成熟度")
    @PostMapping("/AIRipe")
    public R<List<Integer>> AIripe(@RequestParam MultipartFile file,
                                  @ApiParam(value = "conf", defaultValue = "0.25")
                                    @RequestParam(required = false)
                                  @DefaultValue("0.25") double conf,
                                  @ApiParam(value = "iou", defaultValue = "0.5")
                                      @RequestParam(required = false)
                                  @DefaultValue("0.5") double iou) {

        return fruitTreeService.AIOfRipeness(file, conf, iou);
    }
}
