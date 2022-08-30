package com.reggie.Controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;

import com.reggie.Common.BaseContext;
import com.reggie.Common.R;
import com.reggie.Entity.ShoppingCart;
import com.reggie.Service.ShoppingCartService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/shoppingCart")
public class ShoppingCartController {
    @Autowired
    private ShoppingCartService shoppingCartService;
    /*
        查询当前用户的购物车：每个用户都只有一个购物车，数据库中表一条数据只是一个购物项，而不是购物车
        List<ShoppingCart> :这个才是购物车
        一个ShoppingCart:只是一个购物项
     */
    @GetMapping("/list")
    public R<List<ShoppingCart>> list(){
        //1.获得当前用户的id
        Long userId = BaseContext.getCurrentId();
        //2.根据userId查询当前用户的购物车集合
        LambdaQueryWrapper<ShoppingCart> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(ShoppingCart::getUserId,userId);
        //select * from shoppingCart where user_id=?
        List<ShoppingCart> list = shoppingCartService.list(queryWrapper);
        return R.success(list);
    }

    @PostMapping("/add")
    public R<ShoppingCart> add(@RequestBody ShoppingCart shoppingCart){
        //因为响应的数据，页面要展示number，要返回最新的带number的购物项数据
        ShoppingCart cart =  shoppingCartService.saveShoppingCart(shoppingCart);
        return R.success(cart);
    }

    /*
        减的是套餐  setmealId
        减的是菜品  dishId
     */
    @PostMapping("/sub")
    public R<ShoppingCart> sub(@RequestBody ShoppingCart shoppingCart){
        ShoppingCart cart = shoppingCartService.sub(shoppingCart);
        return R.success(cart);
    }

    @DeleteMapping("/clean")
    public R<String> clean(){
        //删除当前用户的所有购物项
        LambdaUpdateWrapper<ShoppingCart> updateWrapper = new LambdaUpdateWrapper<>();
        updateWrapper.eq(ShoppingCart::getUserId,BaseContext.getCurrentId());
        //delete from shoppingCart where user_id = ?
        shoppingCartService.remove(updateWrapper);
        return R.success("清空购物车成功");
    }



}
