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
import com.jhc.fruit.service.AreaService;
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
    private FertWaterDao fertWaterDao;
    @Autowired
    private AreaService areaService;

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
    public R<FruitTreeVo> treeInfo(Long treeId) {
        try{
            FruitTree fruitTree = fruitTreeDao.selectById(treeId);
            FruitTreeVo fruitTreeVo = fruitToVo(fruitTree);

            LambdaQueryWrapper<FertWater> queryWrapper = new LambdaQueryWrapper<>();
            queryWrapper.eq(FertWater::getTreeId, treeId);
            List<FertWater> fertWaters = fertWaterDao.selectList(queryWrapper);

            fruitTreeVo.setLogs(fertWaters);
            fruitTreeVo.setAreaName(areaService.getAreaNameById(fruitTree.getAreaId()));
            fruitTreeVo.setAreaManagerName(fruitTreeDao.getAreaManagerName(fruitTree.getAreaId()));
            fruitTreeVo.setOrchardName(fruitTreeDao.getOrchardNameById(fruitTree.getOrchardId()));
            return R.success(fruitTreeVo);
        }catch (Exception e){
            e.printStackTrace();
            return R.error("数据查询失败");
        }


    }
    @Override
    public R<FruitTreeVo> treeInfoSevenDays(Long treeId) {
        try{
            FruitTree fruitTree = fruitTreeDao.selectById(treeId);
            FruitTreeVo fruitTreeVo = fruitToVo(fruitTree);

            LambdaQueryWrapper<FertWater> queryWrapper = new LambdaQueryWrapper<>();
            queryWrapper.eq(FertWater::getTreeId, treeId);
            //查询最近7天的记录
            queryWrapper.ge(FertWater::getCreateTime, System.currentTimeMillis() - 7L * 24 * 60 * 60 * 1000);
            List<FertWater> fertWaters = fertWaterDao.selectList(queryWrapper);

            fruitTreeVo.setLogs(fertWaters);
            fruitTreeVo.setAreaName(areaService.getAreaNameById(fruitTree.getAreaId()));
            fruitTreeVo.setAreaManagerName(fruitTreeDao.getAreaManagerName(fruitTree.getAreaId()));
            fruitTreeVo.setOrchardName(fruitTreeDao.getOrchardNameById(fruitTree.getOrchardId()));
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
            String imageUrl = fileService.getImageUrl(url);
            QRUrls.add(imageUrl);
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
