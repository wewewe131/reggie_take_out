package com.reggie.Common;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.sql.SQLIntegrityConstraintViolationException;

/**
 * @author Reggie
 * @description 全局异常处理
 * @date 2022/8/15
 */
@RestControllerAdvice(annotations = {RestController.class, Controller.class})//添加了这两个注解的类，都会拦截异常
@Slf4j
public class GlobalExceptionHandler {
    @ExceptionHandler(SQLIntegrityConstraintViolationException.class)
    public R handleSqlException(SQLIntegrityConstraintViolationException e) {
        log.error("数据库异常", e);

        if (e.getMessage().contains("Duplicate entry")) {//捕获数据库抛出的异常，如果是重复插入数据，则返回错误信息
            String [] split = e.getMessage().split(" ");
            String msg = split[2]+"已存在";
            return R.error(msg);
        }

        return R.error("数据库异常");
    }
}
