package com.jhc.fruit.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.jhc.fruit.entity.FruitTree;
import com.jhc.fruit.entity.FruitType;
import com.jhc.fruit.entity.vo.FruitTreeVo;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import java.util.List;

@Mapper
public interface FruitTreeDao extends BaseMapper<FruitTree> {
    /**
     * 由于水果种类不能以种类id形式展露【可读性太差】
     */
    public List<FruitTreeVo> pageSelect(Page<FruitTreeVo> page, String type);
//    public List<Integer> getAreaId();
    public Long getTypeId(Long areaId);
    public List<FruitType> showType();
    public FruitTree getById(Long id);
    public List<FruitTree> selectByAreaIdList(Long areaId);
    public List<FruitTreeVo> sortByRipeCount(Long areaId);
    public List<FruitTreeVo> sortByCount(Long areaId);
    public FruitTree selectBy2Id(Long id);
    // public int updateById(FruitTree fruitTree);
    @Select("SELECT id,type,count_num,ripe_num,img,area_id FROM fruit_tree")
    public Page<FruitTreeVo> page(Page<FruitTreeVo> page);

    String selectByAreaId(Long areaId);

    String selectByType(Long type);

    List<FruitTree> myTree(Long id);

    boolean checkEmpOfTree(Long userId, Long treeId);

    void saveQRUrl(Long id, String url);

    String selectQR(Long id);

    void deleteByAreaId(Long id);

    String getOrchardNameById(Long orchardId);

    String getAreaManagerName(Long areaId);

    List<Long> selectTypeDISTINCT(Long orchardId);

    Integer countType(Long orchardId, Long typeId);

    List<FruitTree> selectByTaskType(Long orchardId, String type);

    Integer getRipeSum(Long orchardId, Long typeId);

    Integer getCountNumSum(Long orchardId, Long typeId);

    Integer getAreaRipeSum(Long orchardId, Long areaId);

    Integer getAreaCountNumSum(Long orchardId, Long id);

    Long selectOrchardIdByTreeId(Long treeId);
}
