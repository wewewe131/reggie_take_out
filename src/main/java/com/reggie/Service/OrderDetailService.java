package com.reggie.Service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.reggie.Entity.OrderDetail;

/*
    mybatisplus管得很宽，明明是持久层框架，但是他把service也给管了
    当前的service是操作employee模块的service
 */
public interface OrderDetailService extends IService<OrderDetail> {

}
