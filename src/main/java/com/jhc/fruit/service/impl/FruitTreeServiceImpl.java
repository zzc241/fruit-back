package com.jhc.fruit.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.jhc.fruit.common.QRCodeUtil;
import com.jhc.fruit.common.R;
import com.jhc.fruit.dao.FruitTreeDao;
import com.jhc.fruit.entity.FruitTree;
import com.jhc.fruit.entity.FruitType;
import com.jhc.fruit.entity.vo.FruitTreeVo;
import com.jhc.fruit.entity.vo.QRTreeVo;
import com.jhc.fruit.service.FileService;
import com.jhc.fruit.service.FruitTreeService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

/**
 * @author ：lcy
 * @description：TODO
 * @date ：2022/5/17 0017 9:42
 */
@Service
public class FruitTreeServiceImpl extends ServiceImpl<FruitTreeDao, FruitTree> implements FruitTreeService {

    @Autowired
    private FruitTreeDao fruitTreeDao;
    @Autowired
    private FileService fileService;

    @Override
    public Page<FruitTreeVo> page(Page<FruitTreeVo> page) {
        Page<FruitTreeVo> pageInfo = fruitTreeDao.page(page);
        return pageInfo;
    }

    //解析二维码，返回果树信息
    @Override
    public R<FruitTreeVo> showTree(QRTreeVo eqTreeVo) {
        //解析二维码
        InputStream inputStream = fileService.getMinioFileInputStream(eqTreeVo.getFileName());

        //查找果树信息
        FruitTree fruitTree = fruitTreeDao.selectById(QRCodeUtil.readQRCode(inputStream));
        FruitTreeVo fruitTreeVo = fruitToVo(fruitTree);
        return R.success(fruitTreeVo);
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
        return fruitTreeVo;
    }

    public FruitTree voToFruitTree(FruitTreeVo fruitTreeVo) {
        FruitTree fruitTree = new FruitTree();
        BeanUtils.copyProperties(fruitTreeVo, fruitTree);
        Long typeId = fruitTreeVo.getTypeId();
        fruitTree.setType(typeId);
        return fruitTree;
    }
}
