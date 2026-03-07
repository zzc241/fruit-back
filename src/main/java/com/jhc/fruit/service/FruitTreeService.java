package com.jhc.fruit.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.jhc.fruit.common.R;
import com.jhc.fruit.entity.FruitTree;
import com.jhc.fruit.entity.FruitType;
import com.jhc.fruit.entity.AnnounceInfo;
import com.jhc.fruit.entity.vo.FruitTreeVo;
import com.jhc.fruit.entity.vo.QRTreeVo;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface FruitTreeService extends IService<FruitTree> {
    public List<FruitTreeVo> pageSelect(Page<FruitTreeVo> page,String type);
    public Long getTypeId(Long areaId);
    public List<FruitType> showType();
    public FruitTree getById(Long id);
    public List<FruitTree> selectByAreaIdList(Long areaId);
    public List<FruitTreeVo> sortByRipeCount(Long areaId);
    public List<FruitTreeVo> sortByCount(Long areaId);
    public FruitTree selectBy2Id(Long id);
    public boolean updateById(FruitTree fruitTree);
    public Page<FruitTreeVo> page(Page<FruitTreeVo> page);

    R<FruitTreeVo> showTree(MultipartFile file);

    R<List<FruitTreeVo>> myTree(Long id);

    R<FruitTreeVo> myTreeInfo(Long userId, Long treeId);

    List<FruitTree> getByIds(List<Long> ids);

    R<List<Integer>> AIOfRipeness(MultipartFile file, double conf, double iou);

    R<FruitTreeVo> treeInfo(Long treeId);
    R<List<String>> createTreeQR(List<Long> ids);

    R<List<String>> selectQR(List<Long> ids);
}
