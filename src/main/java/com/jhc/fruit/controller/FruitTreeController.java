package com.jhc.fruit.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.jhc.fruit.aop.Log;
import com.jhc.fruit.common.BaseContext;
import com.jhc.fruit.common.QRCodeUtil;
import com.jhc.fruit.common.QiniuUtils;
import com.jhc.fruit.common.R;
import com.jhc.fruit.entity.*;
import com.jhc.fruit.entity.vo.FruitTreeVo;
import com.jhc.fruit.service.impl.AreaServiceImpl;
import com.jhc.fruit.service.impl.FruitGrowServiceImpl;
import com.jhc.fruit.service.impl.FruitTreeServiceImpl;
import com.jhc.fruit.service.impl.FruitTypeServiceImpl;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.Map;

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
    private FruitTreeServiceImpl fruitTreeServiceImpl;

    @Autowired
    private FruitGrowServiceImpl fruitGrowService;

    @Autowired
    private FruitTypeServiceImpl fruitTypeServiceImpl;



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
        pageInfo.setTotal(fruitTreeServiceImpl.page(page1).getTotal());
        pageInfo.setPages(fruitTreeServiceImpl.page(page1).getPages());
        List<FruitTreeVo> result = fruitTreeServiceImpl.pageSelect(pageInfo, type);
        pageInfo.setRecords(result);

        System.out.println(pageInfo.getRecords());
        return R.success(pageInfo);
    }

    /**
     * 新增果树功能
     * @param fruitTree
     * @return
     */
    @Log
    @PostMapping
    @ApiOperation("添加果树方法")
    public R<String> save(@RequestBody FruitTree fruitTree){
        //1.获取添加果树的种类，判断是否符合种在对应区块
        Long typeId = fruitTree.getType();
        //获取区号
        Long areaId = (long) fruitTree.getAreaId();
        //获取对应区块种植的蔬菜
        Long trueTypeId = fruitTreeServiceImpl.getTypeId(areaId);
        //判断添加的果树与对应区块种植的果树的类型是否相同
        //如果果树类型不同
        if(!typeId.equals(trueTypeId)){
            return R.error("当前区块不能添加种植此类型作物");
        }
        //如果类型相同
        FruitTree fruitTreeSave = new FruitTree();
        BeanUtils.copyProperties(fruitTree, fruitTreeSave);
        //添加果蔬
        boolean treeSave = fruitTreeServiceImpl.save(fruitTreeSave);
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
        FruitTree fruitTree = fruitTreeServiceImpl.getById(id);
        FruitTreeVo fruitTreeVo = new FruitTreeVo();
        BeanUtils.copyProperties(fruitTree, fruitTreeVo);
        fruitTreeVo.setTypen(fruitTypeServiceImpl.getTypeById(fruitTree.getType()));
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
    @Log
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
        boolean b = fruitTreeServiceImpl.updateById(fruitTree);
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
    @Log
    @DeleteMapping("/{id}")
    @ApiOperation("根据id删除果树")
    @ApiImplicitParam(name="id",value ="要删除果树的对应id")
    public R<String> delete(@PathVariable Long id){
        FruitTree fruitTree = fruitTreeServiceImpl.getById(id);
        if(fruitTree == null){
            return R.error("找不到当前果树");
        }
        //删除对应图片
        QiniuUtils.deleteFileFromQiniu(fruitTree.getImg());
        FruitGrow fruitGrow = fruitGrowService.getById(id);
 /*     原逻辑下需要关联果树生长表才能删除，暂时先注释掉
        if(fruitGrow != null){
            String firstTerm = fruitGrow.getFirstTerm();

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
            }
            //删除对应生长过程
            fruitGrowService.removeById(fruitGrow.getTreeId());
        fruitTreeServiceImpl.removeById(id);
        }*/
        fruitTreeServiceImpl.removeById(id);
        return R.success("删除成功");
    }

    @GetMapping("/showType")
    @ApiOperation("展示果树类型的数据[id + 类型]")
    public R<List<FruitType>> showType(){
        List<FruitType> fruitTypes = fruitTreeServiceImpl.showType();
        return R.success(fruitTypes);
    }

    @GetMapping("/select/{areaId}")
    @ApiOperation("根据区号展示果树")
    @ApiImplicitParam(name="areaId",value ="区块id")
    public R<List<FruitTree>> selectByAreaId(int areaId){
        List<FruitTree> fruitTreeList = fruitTreeServiceImpl.selectByAreaId(areaId);
        if(fruitTreeList == null && fruitTreeList.size() == 0){
            return R.error("查找失败");
        }
        return R.success(fruitTreeList);
    }

    @GetMapping("/select/sortByCount/{areaId}")
    @ApiOperation("根据区号展示果树完后按果实数量降序")
    @ApiImplicitParam(name="areaId",value ="区块id")
    public R<List<FruitTreeVo>> sortByCount(@PathVariable Long areaId){
        List<FruitTreeVo> fruitTreeList = fruitTreeServiceImpl.sortByCount(areaId);
        if(fruitTreeList == null && fruitTreeList.size() == 0){
            return R.error("查找失败");
        }
        return R.success(fruitTreeList);
    }

    @GetMapping("/select/sortByRipeCount/{areaId}")
    @ApiOperation("根据区号展示果树完后成熟果实实数量降序")
    @ApiImplicitParam(name="areaId",value ="区块id")
    public R<List<FruitTreeVo>> sortByRipeCount(@PathVariable int areaId){
        List<FruitTreeVo> fruitTreeList = fruitTreeServiceImpl.sortByRipeCount(areaId);
        if(fruitTreeList == null && fruitTreeList.size() == 0){
            return R.error("查找失败");
        }
        return R.success(fruitTreeList);
    }
}
