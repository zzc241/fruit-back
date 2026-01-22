package com.jhc.fruit.entity;

import lombok.Data;

/**
 * @author ：lcy
 * @description：TODO
 * @date ：2022/6/13 0013 9:25
 */
@Data
public class EmpGroup {
    private Long groupId;
    private Long empId;
    private String content;
    private Long infoId;
    private Employee employee;
    private Boolean isFinish;
}
