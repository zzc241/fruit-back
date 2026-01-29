package com.jhc.fruit.entity;

import lombok.Data;

import java.time.LocalDateTime;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;

@Data
@TableName("operation_log")
public class OperationLog {
    //系统操作日志类
    @TableId(type = IdType.AUTO)
    private Long id;
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
