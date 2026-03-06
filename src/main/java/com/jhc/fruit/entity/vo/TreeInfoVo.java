package com.jhc.fruit.entity.vo;

import lombok.Data;
import java.util.Date;

@Data
public class TreeInfoVo {
    // 来自 fruit_tree 表
    private String treeId;
    private String treeName;
    private Integer age;
    private String address;
    private Date createTime;
    private Date updateTime;

    // 来自 fruit_type 表
    private String type;

    // 来自 fruit_grow 表
    private String firstTerm;
    private String secondTerm;
    private String thirdTerm;
}
