package com.jhc.fruit.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.jhc.fruit.common.QiniuUtils;
import com.jhc.fruit.common.R;
import com.jhc.fruit.entity.Area;
import com.jhc.fruit.entity.Camera;
import com.jhc.fruit.entity.FruitType;
import com.jhc.fruit.entity.vo.AreaVo;
import com.jhc.fruit.service.impl.AreaServiceImpl;
import com.jhc.fruit.service.impl.CameraServiceImpl;
import com.jhc.fruit.service.impl.FruitTypeServiceImpl;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

/**
 * @author ：lcy
 * @description：果园地区管理相关接口
 * @date ：2022/5/9 0009 15:34
 */
@RestController
@RequestMapping("/area")
@Api(tags = "地区管理接口")
public class AreaController {
    @Autowired
    private AreaServiceImpl areaServiceImpl;

    @Autowired
    private CameraServiceImpl cameraServiceImpl;

    @Autowired
    private FruitTypeServiceImpl fruitTypeServiceImpl;


    @PostMapping
    @ApiOperation("后：地区增加方法")
    public R<String> save(@RequestBody AreaVo areaVo){
        //获取新增区域的类型
        String type = areaVo.getType();
        //通过前端新增区域里的类型查找对应类型在后端的id
        Long typeId = areaServiceImpl.getIdByType(type);
        System.out.println(typeId);
        //如果该类型不存在
        if(typeId == null){
            return R.error("找不到当前的种类");
        }
        //如果该类型存在
        Area area = new Area();
        //将获取到的id赋予给area表
        area.setTypeId(typeId);
        BeanUtils.copyProperties(areaVo, area);
        //保存
        areaServiceImpl.save(area);
        return R.success("新增地区成功");
    }

    @DeleteMapping("/{id}")
    @ApiOperation("后：地区删除方法")
    @ApiImplicitParam(name = "id",value = "地区id")
    public R<List<Camera>> delete(@PathVariable int id){
        //1.根据地区id删除对应的地区
        areaServiceImpl.removeById(id);
        //2.创建条件构造器，获取属于地区id的piece对象列表
        LambdaQueryWrapper<Camera> lambdaQueryWrapper = new LambdaQueryWrapper<>();
        lambdaQueryWrapper.eq(Camera::getAreaId,id);
        List<Camera> cameras = cameraServiceImpl.list(lambdaQueryWrapper);

        //返回摄像头列表，进行修改摄像头信息
        return R.success(cameras);
    }

    /**
     * 分页查询
     * @param page 当前页
     * @param pageSize 最大页数
     * @return
     */
    @GetMapping
    @ApiOperation("后：查询所有地区以分页展示方法")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "page",value = "当前页码数"),
            @ApiImplicitParam(name = "pageSize",value = "最大页码数"),
            @ApiImplicitParam(name = "name",value = "区块名称模糊查询[可为空]")
    })
    public R<Page> page(int page, int pageSize,String name){
        //创建分页构造器
        Page<AreaVo> pageInfo = new Page(page,pageSize);
        Page<AreaVo> page1 = new Page(page,pageSize);
        //模糊查询,添加符号
        if(name != null){
            name += "%";
        }
        pageInfo.setTotal(areaServiceImpl.page(page1).getTotal());
        pageInfo.setPages(areaServiceImpl.page(page1).getPages());
        List<AreaVo> result = areaServiceImpl.getPageInfo(pageInfo, name);
        //进行分页查询
        pageInfo.setRecords(result);
        return R.success(pageInfo);
    }

    /**
     * 根据id查询
     * @param id
     * @return
     */
    @GetMapping("/{id}")
    @ApiOperation("后：根据id查询地区信息，用于修改地区信息时回溯信息")
    @ApiImplicitParam(name = "id",value = "地区id")
    public R<Area> getById(@PathVariable int id){
        Area area = areaServiceImpl.getById(id);
        return R.success(area);
    }

    @PutMapping
    @ApiOperation("后：修改地区信息")
    public R<String> update(@RequestBody Area area){
        areaServiceImpl.updateById(area);
        return R.success("修改地区成功");
    }

    @GetMapping("/select/{id}")
    @ApiOperation("地区编号[数字]进行筛选")
    @ApiImplicitParam(name = "id",value = "筛选区块编号为多少的区块")
    public R<Area> selectById(@PathVariable int id){
        Area area = areaServiceImpl.getById(id);
        return R.success(area);
    }

    @GetMapping("/getType")
    @ApiOperation("移动端：根据果蔬类型id查找对应果蔬类型,用于筛选时果蔬类别的展示")
    public R<List<String>> selectType(){
        List<String> type = areaServiceImpl.getTypeById();
        if(type == null && type.size() == 0){
            return R.error("获取失败");
        }
        return R.success(type);
    }

    @GetMapping("/getId")
    @ApiOperation("移动端：根据区块id查找对应区块,用于筛选时区块编号的展示")
    public R<List<Integer>> selectId(){
        List<Integer> idList = areaServiceImpl.getId();
        if(idList == null && idList.size() == 0){
            return R.error("获取失败");
        }
        return R.success(idList);
    }

    @GetMapping("/select")
    @ApiOperation("中字类型筛选")
    @ApiImplicitParam(name = "type",value = "果蔬类型")
    public R<List<Area>> selectByType(String type){
        List<Area> areaByType = areaServiceImpl.getAreaByType(type);
        if(areaByType == null && areaByType.size() == 0){
            return R.error("筛选失败");
        }
        return R.success(areaByType);
    }


}

