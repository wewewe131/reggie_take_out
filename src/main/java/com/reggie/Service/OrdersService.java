package com.reggie.Service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.reggie.Entity.Orders;
import com.reggie.dto.OrdersDto;


/*
    mybatisplus管得很宽，明明是持久层框架，但是他把service也给管了
 */
public interface OrdersService extends IService<Orders> {

    void submit(Orders orders);

    Page<OrdersDto> pageOrdersAndOrdersDetail(Integer page, Integer pageSize);

}
