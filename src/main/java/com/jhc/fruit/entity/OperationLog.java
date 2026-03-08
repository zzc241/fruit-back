package com.jhc.fruit.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class OperationLog {
    //系统操作日志类
    @TableId(type = IdType.AUTO)
    private Long id;
//    操作者ID
    private Long userId;
//    操作内容
    private String operationContent;
//    操作时间，默认为系统时间
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss.SSS", timezone = "GMT+8")
    private LocalDateTime operationTime;
//    操作者IP
    private String operationIp;
//    操作返回结果
    private String operationResult;
}
