package com.jhc.fruit.service.impl;

import com.alibaba.fastjson2.JSONArray;
import com.alibaba.fastjson2.JSONException;
import com.alibaba.fastjson2.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.jhc.fruit.common.JsonUtil;
import com.jhc.fruit.common.QRCodeUtil;
import com.jhc.fruit.common.R;
import com.jhc.fruit.config.AIConfig;
import com.jhc.fruit.dao.FertWaterDao;
import com.jhc.fruit.dao.FruitTreeDao;
import com.jhc.fruit.entity.FertWater;
import com.jhc.fruit.entity.FruitTree;
import com.jhc.fruit.entity.FruitType;
import com.jhc.fruit.entity.vo.FruitTreeVo;
import com.jhc.fruit.entity.vo.QRTreeVo;
import com.jhc.fruit.service.FileService;
import com.jhc.fruit.service.FruitTreeService;

import kotlin.jvm.internal.Lambda;
import lombok.extern.slf4j.Slf4j;
import okhttp3.*;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.multipart.MultipartFile;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.UUID;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

/**
 * @author ：lcy
 * @description：TODO
 * @date ：2022/5/17 0017 9:42
 */
@Slf4j
@Service
public class FruitTreeServiceImpl extends ServiceImpl<FruitTreeDao, FruitTree> implements FruitTreeService {

    @Autowired
    private FruitTreeDao fruitTreeDao;
    @Autowired
    private FileService fileService;
    @Autowired
    private AIConfig aiConfig;
    @Autowired
    private FertWaterDao fertWaterDao;
    @Autowired
    private AreaServiceImpl areaServiceImpl;
    //创建客户端实例
    private final OkHttpClient client = new OkHttpClient().newBuilder()
            .connectTimeout(60, TimeUnit.SECONDS)  // 连接超时
            .readTimeout(60, TimeUnit.SECONDS)     // 读取超时
            .writeTimeout(60, TimeUnit.SECONDS)    // 写入超时
            .build();

    @Override
    public Page<FruitTreeVo> page(Page<FruitTreeVo> page) {
        Page<FruitTreeVo> pageInfo = fruitTreeDao.page(page);
        return pageInfo;
    }

    //解析二维码，返回果树信息
    @Override
    public R<FruitTreeVo> showTree(MultipartFile imgFile) {
        //上传二维码
        String url = fileService.upload(imgFile);

        //解析二维码
        InputStream inputStream = fileService.getMinioFileInputStream(url);

        //查找果树信息
        R<FruitTreeVo> result = treeInfo(QRCodeUtil.readTreeQRCode(inputStream));
        return result;
    }

    @Override
    public R<List<FruitTreeVo>> myTree(Long id) {
        //1. 判断id是否存在
        if (fruitTreeDao.selectById(id) == null) {
            return R.error("用户不存在");
        }else {
            //2.如果id存在，查询id对应用户负责的果树列表
            List<FruitTree> fruitTreeList = fruitTreeDao.myTree(id);
            //转换为vo类
            List<FruitTreeVo> fruitTreeVoList = new ArrayList<>();
            fruitTreeList.forEach(fruitTree -> {
                fruitTreeVoList.add(fruitToVo(fruitTree));
            });
            return R.success(fruitTreeVoList);
        }
        //3.返回列表
    }

    @Override
    public R<FruitTreeVo> myTreeInfo(Long userId, Long treeId) {
        FruitTree fruitTree = fruitTreeDao.selectById(treeId);

        if (fruitTree == null) {
            return R.error("果树不存在");
        }
        if (fruitTreeDao.selectById(userId) == null){
            return R.error("用户不存在");
        }
        //检查果树id是否属于该用户
        if (! fruitTreeDao.checkEmpOfTree(userId, treeId)){
            return R.error("非该果树负责人，无权限查看");
        }

        FruitTreeVo fruitTreeVo = fruitToVo(fruitTree);
        return R.success(fruitTreeVo);
    }

    @Override
    public List<FruitTree> getByIds(List<Long> ids) {
        LambdaQueryWrapper<FruitTree> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.in(FruitTree::getId, ids);
        List<FruitTree> fruitTreeList = fruitTreeDao.selectList(queryWrapper);
        return fruitTreeList;
    }

    @Override
    public boolean updateById(FruitTree fruitTree) {
        int i = fruitTreeDao.updateById(fruitTree);
        if(i == 1){
            return true;
        }
        return false;
    }

    @Override
    public FruitTree selectBy2Id(Long id) {
        FruitTree fruitTree = fruitTreeDao.selectBy2Id(id);
        return fruitTree;
    }

    @Override
    public List<FruitTreeVo> pageSelect(Page<FruitTreeVo> pageInfo,String type) {
        List<FruitTreeVo> page = fruitTreeDao.pageSelect(pageInfo,type);
        return page;
    }

    @Override
    public Long getTypeId(Long areaId) {
        Long typeId = fruitTreeDao.getTypeId(areaId);
        return typeId;
    }

    @Override
    public List<FruitType> showType() {
        List<FruitType> typeList = fruitTreeDao.showType();
        return typeList;
    }

    @Override
    public FruitTree getById(Long id) {
        FruitTree fruitTree = fruitTreeDao.getById(id);
        return fruitTree;
    }

    @Override
    public List<FruitTree> selectByAreaIdList(Long areaId) {
        List<FruitTree> fruitTreeList = fruitTreeDao.selectByAreaIdList(areaId);
        return fruitTreeList;
    }

    @Override
    public List<FruitTreeVo> sortByRipeCount(Long areaId) {
        List<FruitTreeVo> fruitTrees = fruitTreeDao.sortByRipeCount(areaId);
        return fruitTrees;
    }

    @Override
    public List<FruitTreeVo> sortByCount(Long areaId) {
        List<FruitTreeVo> fruitTrees = fruitTreeDao.sortByCount(areaId);
        return fruitTrees;
    }

    public FruitTreeVo fruitToVo(FruitTree fruitTree){
        FruitTreeVo fruitTreeVo = new FruitTreeVo();
        BeanUtils.copyProperties(fruitTree,fruitTreeVo);
        fruitTreeVo.setTypeId(fruitTree.getType());
        fruitTreeVo.setType(fruitTreeDao.selectByType(fruitTree.getType()));
        fruitTreeVo.setAreaName(fruitTreeDao.selectByAreaId(fruitTree.getAreaId()));
        if(fruitTreeDao.selectQR(fruitTree.getId()) != null){

            String url = fileService.getImageUrl(fruitTreeDao.selectQR(fruitTree.getId()));
            if(fileService.isPresignedUrlExpired(url)){
                fruitTreeVo.setUrl("URLExpired");
            }else {
                fruitTreeVo.setUrl(url);
            }

        }
        return fruitTreeVo;
    }

    @Override
    public void removeByAreaId(Long id) {
        fruitTreeDao.deleteByAreaId(id);
    }

    public FruitTree voToFruitTree(FruitTreeVo fruitTreeVo) {
        FruitTree fruitTree = new FruitTree();
        BeanUtils.copyProperties(fruitTreeVo, fruitTree);
        Long typeId = fruitTreeVo.getTypeId();
        fruitTree.setType(typeId);
        return fruitTree;
    }

    @Override
    public R<List<Integer>> AIOfRipeness(MultipartFile file,double conf, double iou) {
        List<String> ripes = new ArrayList<>();
        //创建请求对象
        String url = aiConfig.getEndpoint() + "/ripeness/detect/upload" + "?conf=" + conf + "&iou=" + iou;

        File tempFile = null;
        try {
            URL apiURL = new URL(url);
            //封装文件
            tempFile = File.createTempFile("okhttp-upload-", file.getOriginalFilename());
            //将文件内容写入临时文件
            file.transferTo(tempFile);

            MediaType mediaType = MediaType.parse(
                    file.getContentType() != null ? file.getContentType() : "application/octet-stream"
            );
            // 关键：文件上传必须用MultipartBody构建FORM格式请求体
            RequestBody requestBody = new MultipartBody.Builder()
                    .setType(MultipartBody.FORM) // 必须设置为FORM类型
                    .addFormDataPart(
                            "file",                // 文件参数名（AI接口要求的参数名）
                            file.getOriginalFilename(), // 文件名
                            RequestBody.create(tempFile, mediaType) // 文件体
                    )
                    .build();

            //请求参数
            Request request = new Request.Builder().url(apiURL).post(requestBody).build();
            //处理响应数据
            try (Response response = client.newCall(request).execute()) {
                // 第一步：校验响应是否成功
                if (!response.isSuccessful()) {
                    String errorMsg = "AI接口响应失败，状态码：" + response.code();
                    log.error(errorMsg);
                    return R.error(errorMsg);
                }

                // 第二步：安全读取响应体
                ResponseBody responseBody = response.body();
                if (responseBody == null) {
                    log.error("AI接口响应体为空");
                    return R.error("AI接口无响应数据");
                }
                String jsonResult = responseBody.string();
                log.info("AI接口返回原始JSON：{}", jsonResult);

                // 第三步：解析JSON（容错）
                JSONObject res = JSONObject.parseObject(jsonResult);
                if (res == null) {
                    log.error("JSON解析失败，原始数据：{}", jsonResult);
                    return R.error("响应数据格式错误");
                }

                // 第四步：获取数组并判空（核心修复：避免空指针）
                JSONArray ripenessDetections = res.getJSONArray("ripeness_detections");
                if (ripenessDetections != null) {
                    log.info("ripeness detections: " + ripenessDetections.toJSONString());
                    // 遍历赋值
                    if (!ripenessDetections.isEmpty()) {
                        for (int i = 0; i < ripenessDetections.size(); i++) {
                            JSONObject detection = ripenessDetections.getJSONObject(i);
                            if (detection != null) {
                                String ripenessName = detection.getString("ripeness_name");
                                if (ripenessName != null && !ripenessName.isEmpty()) {
                                    ripes.add(ripenessName);
                                } else {
                                    log.warn("第{}条检测结果无成熟度名称", i);
                                }
                            } else {
                                log.warn("第{}条检测结果为null", i);
                            }
                        }
                    } else {
                        log.warn("ripeness_detections数组为空");
                    }
                } else {
                    log.error("JSON中未找到ripeness_detections字段，完整JSON：{}", res.toJSONString());
                    return R.error("AI接口未返回成熟度数据");
                }
            }

            // ========== 修复点5：正确判断列表是否为空 ==========
            if (ripes.isEmpty()) {
                log.warn("未解析到任何成熟度数据");
                return R.error("未检测到果实成熟度信息");
            }

            Integer totalCount = ripes.size();
            Integer ripeCount = 0;
            for (int i = 0; i < ripes.size(); i++) {
                if(ripes.get(i).equals("Ripe")){
                    ripeCount += 1;
                }
            }
            List<Integer> counts = new ArrayList<>();
            counts.add(ripeCount);
            counts.add(totalCount);

            return R.success(counts);
        } catch (IOException e) {
            log.error("网络通信失败", e);
            return R.error("网络通信失败: " + e.getMessage());
        } catch (JSONException e) {
            log.error("JSON解析失败", e);
            return R.error("服务响应格式错误");
        } catch (Exception e) {
            log.error("AI识别异常", e);
            return R.error("系统异常: " + e.getMessage());
        }finally {
            //清理临时文件
            if (tempFile != null) {
                tempFile.delete();
            }
        }
    }

    @Override
    public R<FruitTreeVo> treeInfo(Long treeId) {
        try{
            FruitTree fruitTree = fruitTreeDao.selectById(treeId);
            FruitTreeVo fruitTreeVo = fruitToVo(fruitTree);

            LambdaQueryWrapper<FertWater> queryWrapper = new LambdaQueryWrapper<>();
            queryWrapper.eq(FertWater::getTreeId, treeId);
            List<FertWater> fertWaters = fertWaterDao.selectList(queryWrapper);

            fruitTreeVo.setLogs(fertWaters);
            fruitTreeVo.setAreaManagerName(areaServiceImpl.getAreaNameById(fruitTree.getAreaId()));
            return R.success(fruitTreeVo);
        }catch (Exception e){
            e.printStackTrace();
            return R.error("数据查询失败");
        }


    }
    public R<List<String>> createTreeQRList(List<Long> ids) {
        //先判断该id对应的果树是否存在
        List<FruitTree> fruitTrees = getByIds(ids);
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
        List<String> QRUrls = new ArrayList<>();
        for (FruitTree fruitTree : fruitTrees) {
            Long id = fruitTree.getId();
            String url = fileService.save(
                    QRCodeUtil.createQROfString(String.valueOf(id)), "QRtree");
            QRUrls.add(url);
            fruitTreeDao.saveQRUrl(id, url);
        }

        return R.success(QRUrls);
    }

    @Override
    public R<List<String>> selectQR(List<Long> ids) {
        List<String> urls = new ArrayList<>();
        if (ids == null || ids.isEmpty()) {
            return R.error("果树列表为空");
        }
        for (Long id : ids) {
            String ulr = fruitTreeDao.selectQR(id);
            urls.add(ulr);
        }
        return R.success(urls);
    }


}
