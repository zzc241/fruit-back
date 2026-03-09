package com.jhc.fruit.common;

import com.baomidou.mybatisplus.core.handlers.MetaObjectHandler;
import lombok.extern.slf4j.Slf4j;
import org.apache.ibatis.reflection.MetaObject;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

/**
 * @author ：lcy
 * @description：配置Mybatis Plus自动填充策略
 * @date ：2022/5/3 0003 15:57
 */
@Slf4j
@Component
public class MyMetaObjectHandler implements MetaObjectHandler {
    @Override
    public void insertFill(MetaObject metaObject) {
        log.info("insert自动填充");
        metaObject.setValue("createTime", LocalDateTime.now());
        metaObject.setValue("updateTime", LocalDateTime.now());
    }

    @Override
    public void updateFill(MetaObject metaObject) {
        log.info("update自动填充");
        metaObject.setValue("updateTime", LocalDateTime.now());
    }
}
