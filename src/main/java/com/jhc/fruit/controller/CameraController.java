package com.jhc.fruit.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.jhc.fruit.common.BaseContext;
import com.jhc.fruit.common.CameraUtil;
import com.jhc.fruit.common.QiniuUtils;
import com.jhc.fruit.common.R;
import com.jhc.fruit.entity.Camera;
import com.jhc.fruit.entity.Employee;
import com.jhc.fruit.entity.FruitTree;
import com.jhc.fruit.entity.IdentifyInfo;
import com.jhc.fruit.service.impl.CameraServiceImpl;
import com.jhc.fruit.service.impl.FruitTreeServiceImpl;
import com.jhc.fruit.service.impl.IdentifyInfoServiceImpl;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import java.io.File;
import java.util.ArrayList;
import java.util.List;

/**
 * @author ：lcy
 * @description：摄像头管理
 * @date ：2022/5/3 0003 8:34
 */
@RequestMapping("/camera")
@RestController
@Api(tags = "后台：摄像头管理接口")
public class CameraController {
    @Autowired
    private CameraServiceImpl cameraService;


    @Autowired
    private FruitTreeServiceImpl fruitTreeService;

    @GetMapping("/getTreeIdByArea")
    @ApiOperation("根据摄像头对应的区id获取当中的树")
    public R<List<Long>> getTreeIdByArea(@RequestParam("cid") int cid){
        LambdaQueryWrapper<Camera> wrapperCamera = new LambdaQueryWrapper();
        wrapperCamera.eq(Camera::getId,cid);
        //根据摄像头id获取该摄像头对应的区
        Camera camera = cameraService.getOne(wrapperCamera);
        //获取摄像头对应的区
        Long areaId = camera.getAreaId();
        //查询出区块当中的树对象
        List<FruitTree> fruitTreeList = fruitTreeService.selectByAreaIdList(areaId);

        List<Long> treeIds = new ArrayList<>();
        Long id = null;
        for(FruitTree fruitTree:fruitTreeList){
            id = fruitTree.getId();
            treeIds.add(id);
        }
        return R.success(treeIds);
    }

    /**
     * 摄像头图片上传到服务器,上传成功后得到的地址来识别
     * @param saveUrl
     * @return 服务器图片地址
     */
    @PostMapping("/cameraUp")
    @ApiOperation("摄像头抓拍上传")
    @ApiImplicitParam(name = "saveUrl",value = "抓拍存储到本地的地址")
    public R<String> cameraUp(String saveUrl){
        //1.将本地存放文件的具体路径地址转换为MultipartFile
        MultipartFile multipartFile = QiniuUtils.transform2File(saveUrl);
        //2.上传至服务器
        String fileName = QiniuUtils.formatUpload(multipartFile);
        //3.上传完成后删除本地的文件
        File file = new File(saveUrl);
        file.delete();
        return R.success(fileName);
    }

    @GetMapping("/takePhoto")
    @ApiOperation("后：摄像头拍照")
    public R<String> getPhoto(){
        String photo = CameraUtil.getPhoto();
        return R.success("成功,图片访问地址：" + photo);
    }

    /**
     * 新增摄像头功能
     * @param camera
     * @return
     */
    @PostMapping
    @ApiOperation("后：添加摄像头")
    public R<String> save(@RequestBody Camera camera){
        System.out.println(BaseContext.getId());
        cameraService.save(camera);
        return R.success("新增摄像头成功");
    }

    /**
     * 查询所有摄像头功能（不分页）
     * @return
     */
    @GetMapping
    @ApiOperation("后：展示摄像头的接口(查询所有)")
    public R<Page> select(@RequestParam int page,@RequestParam int pageSize,String id){

        Page<Camera> pageInfo = new Page<>(page,pageSize);
        QueryWrapper<Camera> wrapper = new QueryWrapper();
        wrapper.select().orderByDesc("create_time");
        if(id != null){
            int iId = Integer.parseInt(id);
            if(iId != 0){
                wrapper.eq("id",iId);
            }
        }
        cameraService.page(pageInfo,wrapper);
        return R.success(pageInfo);
    }

    /**
     * 根据id删除摄像头
     * @param id
     * @return
     */
    @DeleteMapping("/{id}")
    @ApiOperation("后：点哪个删哪个[根据id删]")
    @ApiImplicitParam(name="id",value ="要删除摄像头的对应id")
    public R<String> delete(@PathVariable Integer id){
        cameraService.removeById(id);
        return R.success("删除成功");
    }

    /**
     * 根据id查询摄像头信息
     * @param id
     * @return
     */
    @GetMapping("/{id}")
    @ApiOperation("后：修改时回溯信息")
    public R<Camera> getById(@PathVariable Integer id){
        Camera camera = cameraService.getById(id);
        if(camera != null){
            return R.success(camera);
        }
        return R.error("没有查询到对应摄像头信息");
    }

    @PutMapping
    @ApiOperation("后：修改摄像头信息")
    public R<String> update(@RequestBody Camera camera){
        System.out.println(BaseContext.getId());
        boolean isUpdate = cameraService.updateById(camera);
        if(isUpdate){
            return R.success("更新成功");
        }
        return R.error("更新失败");
    }


}
