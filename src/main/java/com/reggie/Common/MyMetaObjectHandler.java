package com.reggie.Common;

import com.baomidou.mybatisplus.core.handlers.MetaObjectHandler;
import lombok.extern.slf4j.Slf4j;
import org.apache.ibatis.reflection.MetaObject;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

/**
 * 自定义mybatisplus公共字段填充,在执行插入操作时，自动填充创建时间和创建人，更新时间和更新人
 * 在执行更新操作时，自动填充更新时间和更新人
 */
@Component
@Slf4j
public class MyMetaObjectHandler implements MetaObjectHandler {
    @Override
    public void insertFill(MetaObject metaObject) {
        long threadId = Thread.currentThread().getId();
        log.info("线程ID：{}",threadId);

        log.info("insert填充");
        metaObject.setValue("createTime", LocalDateTime.now());
        metaObject.setValue("updateTime", LocalDateTime.now());
        metaObject.setValue("createUser",BaseContext.getCurrentId());//获取当前线程中存储的共享变量
        metaObject.setValue("updateUser",BaseContext.getCurrentId());


    }


    @Override
    public void updateFill(MetaObject metaObject) {
        long threadId = Thread.currentThread().getId();
        log.info("线程ID：{}",threadId);

        log.info("update填充");

        metaObject.setValue("updateUser",BaseContext.getCurrentId());
        metaObject.setValue("updateTime", LocalDateTime.now());
    }
}
