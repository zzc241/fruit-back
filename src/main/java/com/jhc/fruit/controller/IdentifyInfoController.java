// package com.jhc.fruit.controller;

// import com.baomidou.mybatisplus.core.conditions.Wrapper;
// import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
// import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
// import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
// import com.jhc.fruit.common.IdentifyUtil;
// // import com.jhc.fruit.common.QiniuUtils;
// import com.jhc.fruit.common.R;
// import com.jhc.fruit.entity.FruitTree;
// import com.jhc.fruit.entity.IdentifyInfo;
// import com.jhc.fruit.service.impl.FruitTreeServiceImpl;
// import com.jhc.fruit.service.impl.IdentifyInfoServiceImpl;
// import io.swagger.annotations.Api;
// import io.swagger.annotations.ApiImplicitParam;
// import io.swagger.annotations.ApiImplicitParams;
// import io.swagger.annotations.ApiOperation;
// import org.jsoup.Jsoup;
// import org.jsoup.nodes.Document;
// import org.jsoup.select.Elements;
// import org.springframework.beans.factory.annotation.Autowired;
// import org.springframework.web.bind.annotation.*;
// import org.springframework.web.multipart.MultipartFile;

// import java.net.URL;
// import java.util.List;

// /**
//  * @author ：lcy
//  * @description：TODO
//  * @date ：2022/5/10 0010 14:03
//  */
// @RestController
// @RequestMapping("/identify")
// @Api(tags = "后台：识别信息管理相关操作")
// public class IdentifyInfoController {

//     @Autowired
//     private IdentifyInfoServiceImpl identifyInfoServiceImpl;

//     @Autowired
//     private FruitTreeServiceImpl fruitTreeServiceImpl;



//     /**
//      * 根据选中识别信息id删除
//      * @param id
//      * @return
//      */
//     @DeleteMapping("/{id}")
//     @ApiOperation("删除识别信息方法")
//     public R<String> delete(@PathVariable Long id){
//         identifyInfoServiceImpl.removeById(id);
//         return R.success("删除识别信息成功");
//     }

//     @GetMapping
//     @ApiOperation("分页查询识别信息方法")
//     @ApiImplicitParams({
//             @ApiImplicitParam(name="page",value="当前页码"),
//             @ApiImplicitParam(name = "pageSize",value = "最大页码")
//     })
//     public R<Page> page(int page, int pageSize){
//         Page pageInfo = new Page(page,pageSize);
//         QueryWrapper<IdentifyInfo> wrapper = new QueryWrapper();
//         wrapper.select().orderByDesc("create_time");
//         identifyInfoServiceImpl.page(pageInfo,wrapper);
//         return R.success(pageInfo);
//     }

//     /**
//      * 用户上传要识别的图片
//      * @param img
//      * @return
//      */
//     @PostMapping("/imgIdentify")
//     @ApiOperation("图片识别")
//     public R<IdentifyInfo> identifyByImg(MultipartFile img,Long treeId) throws Exception{  //获取到前端上传图片的MultipartFile对象
//         //查找对应的果树
//         FruitTree fruitTree = fruitTreeServiceImpl.selectBy2Id(treeId);
//         //将获取到的MultipartFile上传到七牛云
//         String fileName = QiniuUtils.formatUpload(img);
//         //识别后获得到识别信息对象
//         IdentifyInfo identify = IdentifyUtil.identify(fileName);
//         if(identify == null){
//             return R.error("识别失败");
//         }
//         //服务器上存储的识别后图片地址
//         String count_url = QiniuUtils.downloadImg2Cloud(identify.getImgCount());
//         String ripe_url = QiniuUtils.downloadImg2Cloud(identify.getImgRipe());
//         //更新果树识别完后的信息
//         fruitTree.setRipeNum(identify.getRipeNum());
//         fruitTree.setImg(ripe_url);
//         fruitTree.setCountNum((long) identify.getFruitNum());
//         fruitTreeServiceImpl.updateById(fruitTree);
//         //将数据封装到识别信息对象中
//         identify.setImgCount(count_url);
//         identify.setImgRipe(ripe_url);
//         identify.setTreeId(treeId);
//         identifyInfoServiceImpl.save(identify);
//         return R.success(identify);
//     }

//     @PostMapping("/img2url")
//     @ApiOperation("将图片上传服务器返回地址")
//     public R<String> img2url(@RequestParam("file") MultipartFile img){
//         String fileName = QiniuUtils.formatUpload(img);
//         return R.success(fileName);
//     }

//     /**
//      * 用户上传要识别的图片
//      * @param fruitTree
//      * @return
//      */
//     @PostMapping("/urlIdentify")
//     @ApiOperation("图片地址识别")         //@RequestParam("img")String img,@RequestParam("treeId")Long treeId
//     public R<IdentifyInfo> identifyByUrl(@RequestBody FruitTree fruitTree) throws Exception{  //获取到前端上传图片的MultipartFile对象
//         //取出传入的图片地址
//         String img = fruitTree.getImg();
//         //查找对应的果树
//         fruitTree = fruitTreeServiceImpl.selectBy2Id(fruitTree.getId());
//         // 开始时间
//         long stime = System.currentTimeMillis();
//         //识别后获得到识别信息对象
//         IdentifyInfo identify = IdentifyUtil.identify(img);
//         // 结束时间
//         long etime = System.currentTimeMillis();
//         // 计算执行时间
//         System.out.printf("识别方法的执行时长：%d 毫秒.", (etime - stime));

//         //服务器上存储的识别后图片地址
//         String count_url = QiniuUtils.downloadImg2Cloud(identify.getImgCount());
//         String ripe_url = QiniuUtils.downloadImg2Cloud(identify.getImgRipe());
//         //更新果树识别完后的信息
//         fruitTree.setRipeNum(identify.getRipeNum());
//         fruitTree.setCountNum((long) identify.getFruitNum());
//         fruitTree.setImg(ripe_url);
//         fruitTreeServiceImpl.updateById(fruitTree);
//         //将数据封装到识别信息对象中
//         identify.setImgCount(count_url);
//         identify.setImgRipe(ripe_url);
//         identify.setTreeId(fruitTree.getId());
//         identifyInfoServiceImpl.save(identify);
//         return R.success(identify);
//     }
// }
