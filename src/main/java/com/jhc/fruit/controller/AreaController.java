package com.jhc.fruit.controller;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.jhc.fruit.common.R;
import com.jhc.fruit.entity.Area;
import com.jhc.fruit.entity.Camera;
import com.jhc.fruit.entity.Employee;
import com.jhc.fruit.entity.FruitTree;
import com.jhc.fruit.entity.vo.AreaVo;
import com.jhc.fruit.service.AreaService;
import com.jhc.fruit.service.FruitTreeService;
import com.jhc.fruit.service.FruitTypeService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

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
    private AreaService areaService;

    @Autowired
    private FruitTypeService fruitTypeService;
    @Autowired
    private FruitTreeService fruitTreeService;


    @PostMapping
    @ApiOperation("地区增加方法")
    public R<Long> save(@RequestBody AreaVo areaVo){
        //获取新增区域的类型
        String type = areaVo.getType();
        //通过前端新增区域里的类型查找对应类型在后端的id
        Long typeId = areaService.getIdByType(type);
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
        areaService.save(area);
        Long areaId = area.getId();
        Long fruitCount = areaVo.getFruitCount();
        Long fruitId = fruitTypeService.getIdByType(type);
        if(fruitCount != null && fruitCount > 0){
            for(int i = 0; i < fruitCount; i++){ 
                FruitTree fruitTree = new FruitTree();
                fruitTree.setAreaId(areaId);
                fruitTree.setType(fruitId);
                fruitTreeService.save(fruitTree);
            }
        }
        Long id = area.getId();
        return R.success(id);
    }

    @DeleteMapping("/{id}")
    @ApiOperation("地区删除方法")
    @ApiImplicitParam(name = "id",value = "地区id")
    public R<String> delete(@PathVariable Long id){
        //1.根据地区id删除对应的地区
        areaService.removeArea(id);

        return R.success();
    }

    /**
     * 分页查询
     * @param page 当前页
     * @param pageSize 最大页数
     * @return
     */
    @GetMapping
    @ApiOperation("查询所有地区以分页展示方法")
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
        pageInfo.setTotal(areaService.page(page1).getTotal());
        pageInfo.setPages(areaService.page(page1).getPages());
        List<AreaVo> result = areaService.getPageInfo(pageInfo, name);
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
    @ApiOperation("根据id查询地区信息，用于修改地区信息时回溯信息")
    @ApiImplicitParam(name = "id",value = "地区id")
    public R<AreaVo> getById(@PathVariable int id){
        Area area = areaService.getById(id);
        AreaVo areaVo = areaService.areaToVo(area);
        return R.success(areaVo);
    }

    @PutMapping
    @ApiOperation("修改地区信息")
    public R<String> update(@RequestBody Area area){
        areaService.updateById(area);
        return R.success("修改地区成功");
    }

    @GetMapping("/getType")
    @ApiOperation("根据果蔬类型id查找对应果蔬类型,用于筛选时果蔬类别的展示")
    public R<List<String>> selectType(){
        List<String> type = areaService.getTypeById();
        if(type == null && type.isEmpty()){
            return R.error("获取失败");
        }
        return R.success(type);
    }

    @GetMapping("/getId")
    @ApiOperation("获取区域id列表")
    public R<List<Integer>> selectId(){
        List<Integer> idList = areaService.getId();
        if(idList == null && idList.isEmpty()){
            return R.error("获取失败");
        }
        return R.success(idList);
    }

    @GetMapping("/select")
    @ApiOperation("类型筛选")
    @ApiImplicitParam(name = "type",value = "果蔬类型")
    public R<List<Area>> selectByType(String type){
        List<Area> areaByType = areaService.getAreaByType(type);
        if(areaByType == null && areaByType.isEmpty()){
            return R.error("筛选失败");
        }
        return R.success(areaByType);
    }

    @GetMapping("/selectByOrchardId")
    @ApiOperation("根据果园id筛选")
    @ApiImplicitParam(name = "orchardId",value = "果园id")
    public R<List<AreaVo>> selectByOrchardId(Long orchardId){
        List<AreaVo> areaByOrchardId = areaService.selectByOrchardId(orchardId);
        return R.success(areaByOrchardId);
    }

    @PutMapping("/addEmp")
    @ApiOperation("添加员工")
    public R<String> addEmployee(@RequestParam Long areaId, @RequestParam Long empId){
        return areaService.addEmployee(empId,areaId);
    }

}

