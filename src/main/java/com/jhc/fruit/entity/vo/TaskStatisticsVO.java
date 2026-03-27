package com.jhc.fruit.entity.vo;

import lombok.Data;

import java.math.BigDecimal;

/**
 * 任务统计信息实体类（用于页面数据回显）
 */
@Data
public class TaskStatisticsVO {

    /**
     * 待派发任务数
     */
    private Integer pendingDispatchCount;

    /**
     * 待执行任务数
     */
    private Integer pendingExecuteCount;

    /**
     * 待执行任务剩余比例（%）
     */
    private Double pendingExecuteRemainingRate;

    /**
     * 已完成任务数
     */
    private Integer completedCount;

    /**
     * 任务完成率（%）
     */
    private Double completionRate;

    /**
     * 逾期任务数
     */
    private Integer overdueCount;

}