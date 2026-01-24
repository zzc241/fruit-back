package com.jhc.fruit.entity;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class OperationLog {
    //系统操作日志类
    private Long logId;
//    操作者ID
    private Long userId;
//    操作内容
    private String operationContent;
//    操作时间，默认为系统时间
    private LocalDateTime operationTime;
//    操作者IP
    private String operationIp;
//    操作返回结果
    private String operationResult;
}
