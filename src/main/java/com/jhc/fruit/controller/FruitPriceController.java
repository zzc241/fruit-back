package com.jhc.fruit.controller;

import com.jhc.fruit.common.PriceUtil;
import com.jhc.fruit.common.R;
import com.jhc.fruit.entity.FruitPrice;
import com.jhc.fruit.service.FruitPriceService;
import com.jhc.fruit.service.impl.FruitPriceServiceImpl;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/price") // 添加/api前缀
@Api(tags = "水果价格接口")
public class FruitPriceController {

    @Autowired
    private FruitPriceService fruitPriceService;

    /**
     * 获取指定水果的实时收购价格
     * @param fruitName 水果名称
     * @return 价格信息列表
     */
    @ApiOperation("获取指定水果的实时收购价格")
    @PostMapping("/realtime")
    public R<List<FruitPrice>> getRealtimePrice(
            @ApiParam(defaultValue = "柑桔")
            @RequestParam String fruitName) {
        if(null==fruitName||"".equals(fruitName)){
            return R.error("请指定获取水果类型");
        }
        List<FruitPrice> priceList = fruitPriceService.getFruitPrice(fruitName);
        if (priceList == null || priceList.size() == 0){
            return R.error("没有相关数据");
        }
        return R.success(priceList);
    }

    /**
     * 保存水果价格信息
     * @param fruitPrice 价格信息
     * @return 保存结果
     */
    @ApiOperation("保存水果价格信息")
    @PostMapping
    public R<String> savePrice(@RequestBody FruitPrice fruitPrice) {
        fruitPriceService.save(fruitPrice);
        return R.success("保存成功");
    }

    /**
     * 获取所有水果价格信息
     * @return 价格信息列表
     */
    @ApiOperation("获取所有水果价格信息")
    @GetMapping("/all")
    public R<List<FruitPrice>> getAllPrices() {
        List<FruitPrice> priceList = fruitPriceService.list();
        return R.success(priceList);
    }

    @ApiOperation("根据地区查询")
    @PostMapping("/address")
    public R<List<FruitPrice>> selectOfAddress(@RequestParam String address) {
        List<FruitPrice> list = fruitPriceService.selectOfAddress(address);
        return R.success(list);
    }
}