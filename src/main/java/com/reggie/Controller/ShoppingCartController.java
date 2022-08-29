package com.reggie.Controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.reggie.Common.BaseContext;
import com.reggie.Common.R;
import com.reggie.Entity.ShoppingCart;
import com.reggie.Service.ShoppingCartService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@RequestMapping("/shoppingCart")
@RestController
@Slf4j
public class ShoppingCartController {

    private final ShoppingCartService shoppingCartService;
    @Autowired
    public ShoppingCartController(ShoppingCartService shoppingCartService) {
        this.shoppingCartService = shoppingCartService;
    }

    @GetMapping("/list")
    public R<List<ShoppingCart>> list() {
        LambdaQueryWrapper<ShoppingCart> lambdaQueryWrapper = new LambdaQueryWrapper<>();
        lambdaQueryWrapper.eq(ShoppingCart::getUserId, BaseContext.getCurrentId());
        lambdaQueryWrapper.orderByAsc(ShoppingCart::getCreateTime);
        List<ShoppingCart> shoppingCarts = shoppingCartService.list(lambdaQueryWrapper);
        return R.success(shoppingCarts);
    }

    @PostMapping("/add")
    public R<ShoppingCart> shoppingCartR(@RequestBody ShoppingCart shoppingCart){
        log.info(shoppingCart.toString());
        Long userid = BaseContext.getCurrentId();
        shoppingCart.setUserId(userid);

        Long dishId = shoppingCart.getDishId();

        LambdaQueryWrapper<ShoppingCart> lambdaQueryWrapper = new LambdaQueryWrapper<>();
        lambdaQueryWrapper.eq(ShoppingCart::getUserId, userid);
        if (dishId!=null){
            lambdaQueryWrapper.eq(ShoppingCart::getDishId, dishId);
        }else {
            lambdaQueryWrapper.eq(ShoppingCart::getSetmealId, shoppingCart.getSetmealId());
        }
        ShoppingCart one = shoppingCartService.getOne(lambdaQueryWrapper);
        if (one!=null){
            Integer num = one.getNumber();
            one.setNumber(num+1);
            shoppingCartService.updateById(one);
        }else {
            shoppingCart.setNumber(1);
            shoppingCart.setCreateTime(LocalDateTime.now());
            shoppingCartService.save(shoppingCart);
            one = shoppingCart;
        }
        return R.success(one);
    }

}
