package com.jhc.fruit.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.jhc.fruit.common.QiniuUtils;
import com.jhc.fruit.common.R;
import com.jhc.fruit.entity.FruitGrow;
import com.jhc.fruit.service.impl.FruitGrowServiceImpl;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

/**
 * @author ：挽風
 * @description：TODO
 * @date ：2022/6/21 0021 17:15
 */
@RestController
@RequestMapping("/fruitGrow")
@Api(tags="果蔬生长过程管理接口")
public class FruitGrowController {
    @Autowired
    private FruitGrowServiceImpl fruitGrowService;

    @DeleteMapping("/{id}")
    @ApiOperation("删除果蔬生成过程")
    public R<String> deleteGrow(@PathVariable Long id){
        boolean removeById = fruitGrowService.removeById(id);
        if(! removeById){
            return R.error("删除失败");
        }
        return R.success("删除成功");
    }

    /**
     * 新增果树时添加对应生长过程
     * @param id
     * @return 是否添加成功
     */
    @PostMapping
    @ApiOperation("更新果蔬生长过程对象")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "id",value = "果蔬Id"),
            @ApiImplicitParam(name = "multipartFile",value = "生长过程照片"),
            @ApiImplicitParam(name = "termNum",value = "生长第几阶段")
    })
    public R<String> updateGrow(Long id, @RequestParam("imgFile")MultipartFile multipartFile,int termNum){
        LambdaQueryWrapper<FruitGrow> growWrapper = new LambdaQueryWrapper();
        growWrapper.eq(FruitGrow::getId,id);
        FruitGrow fruitGrow = fruitGrowService.getOne(growWrapper);
        if(fruitGrow == null){
            return R.error("无历史数据");
        }
        //将图片存储到服务器
        String fileName = QiniuUtils.formatUpload(multipartFile);
        switch (termNum){
            case 1:
                fruitGrow.setFirstTerm(fileName);
                break;
            case 2:
                fruitGrow.setSecondTerm(fileName);
                break;
            case 3:
                fruitGrow.setThirdTerm(fileName);
                break;
        }
        boolean update = fruitGrowService.update(fruitGrow,growWrapper);
        if(!update){
            return R.error("更新失败");
        }
        return R.success("更新成功");
    }

    @GetMapping
    @ApiOperation("查找果蔬生长过程")
    @ApiImplicitParam
    public R<FruitGrow> getFruitGrow(Long id){
        FruitGrow fruitGrow = fruitGrowService.getById(id);
        if(fruitGrow == null){
            return R.error("查找错误");
        }
        return R.success(fruitGrow);
    }
}
