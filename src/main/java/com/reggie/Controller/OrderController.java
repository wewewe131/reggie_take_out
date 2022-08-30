package com.reggie.Controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;

import com.reggie.Common.R;
import com.reggie.Entity.Orders;
import com.reggie.Service.OrdersService;
import com.reggie.dto.OrdersDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/order")
public class OrderController {
    @Autowired
    private OrdersService ordersService;
    /*
        生成订单基本逻辑：把购物车转成订单并保存订单表，并清空购物车
        分析：

     */
    @PostMapping("/submit")
    public R<String> submit(@RequestBody Orders orders){
        ordersService.submit(orders);
        return R.success("订单生成成功");
    }

    @GetMapping("/userPage")
    public R<Page<OrdersDto>> ordersPage(Integer page, Integer pageSize){
        Page<OrdersDto> pageInfo = ordersService.pageOrdersAndOrdersDetail(page,pageSize);

        return R.success(pageInfo);
    }
}
