package com.jhc.fruit.controller;

import com.jhc.fruit.common.PriceUtil;
import com.jhc.fruit.common.R;
import com.jhc.fruit.entity.FruitPrice;
import com.jhc.fruit.service.impl.FruitPriceServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/price") // 添加/api前缀
public class FruitPriceController {

    @Autowired
    private FruitPriceServiceImpl fruitPriceServiceImpl;

    /**
     * 获取指定水果的实时收购价格
     * @param fruitName 水果名称
     * @return 价格信息列表
     */
    @GetMapping("/realtime")
    public R<List<FruitPrice>> getRealtimePrice(@RequestParam String fruitName) {
        List<FruitPrice> priceList = PriceUtil.getFruitPrice(fruitName);
        return R.success(priceList);
    }

    /**
     * 保存水果价格信息
     * @param fruitPrice 价格信息
     * @return 保存结果
     */
    @PostMapping
    public R<String> savePrice(@RequestBody FruitPrice fruitPrice) {
        fruitPriceServiceImpl.save(fruitPrice);
        return R.success("保存成功");
    }

    /**
     * 获取所有水果价格信息
     * @return 价格信息列表
     */
    @GetMapping
    public R<List<FruitPrice>> getAllPrices() {
        List<FruitPrice> priceList = fruitPriceServiceImpl.list();
        return R.success(priceList);
    }
}