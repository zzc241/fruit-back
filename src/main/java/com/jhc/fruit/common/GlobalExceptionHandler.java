package com.jhc.fruit.common;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import java.sql.SQLIntegrityConstraintViolationException;


/**
 * @description：全局异常处理器
 */
@Slf4j
@ResponseBody
@ControllerAdvice(annotations={RestController.class, Controller.class})
public class GlobalExceptionHandler {

    @ExceptionHandler(SQLIntegrityConstraintViolationException.class)
    public R<String> exceptionHandler(SQLIntegrityConstraintViolationException ex){
        log.error(ex.getMessage());
        //根据异常中是否有关键字进行判断
        if(ex.getMessage().contains("Duplicate entry")){
            String[] split = ex.getMessage().split(" ");
            String msg = split[2] + "已存在";
            return R.error(msg);
        }
        if(ex.getMessage().contains("Connection timed out")){
            return R.error("当前服务连接超时");
        }
        if(ex.getMessage().contains("foreign key")){
            return R.error("当前组任务尚未结束，该通知无法删除");
        }
        return R.error("未知错误");
    }
}
