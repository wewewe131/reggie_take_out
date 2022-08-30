package com.reggie.Service.Impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.reggie.Common.BaseContext;
import com.reggie.Entity.ShoppingCart;
import com.reggie.Mapper.ShoppingCartMapper;
import com.reggie.Service.ShoppingCartService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class ShoppingCartServiceImpl extends ServiceImpl<ShoppingCartMapper, ShoppingCart> implements ShoppingCartService {
    @Override 
    public ShoppingCart saveShoppingCart(ShoppingCart shoppingCart) {
        //1.根据用户id和当前菜品id（也有可能是套餐id） 查询购物车
        LambdaQueryWrapper<ShoppingCart> queryWrapper1 = new LambdaQueryWrapper();
        queryWrapper1.eq(ShoppingCart::getUserId, BaseContext.getCurrentId());
        //下面两个条件，只能满足其一，不可能两个都满足
        queryWrapper1.eq(shoppingCart.getDishId()!=null,ShoppingCart::getDishId,shoppingCart.getDishId());
        queryWrapper1.eq(shoppingCart.getSetmealId()!=null,ShoppingCart::getSetmealId,shoppingCart.getSetmealId());
        ShoppingCart  cartForMysql= super.getOne(queryWrapper1);
        //如果能查询到，有这个数据，就做修改number 为+1；
        if(cartForMysql!=null){
            cartForMysql.setNumber(cartForMysql.getNumber()+1);
            super.updateById(cartForMysql);
            return cartForMysql;
        }else {//如果说数据库中没有这个数据，就做新增ShoppingCart
            //完善数据
            shoppingCart.setUserId(BaseContext.getCurrentId());
            shoppingCart.setNumber(1);
            super.save(shoppingCart);
            return shoppingCart;
        }

    }

    @Override
    public ShoppingCart sub(ShoppingCart shoppingCart) {
        //1.根据当前用户id和传入的setmealId(或者是dishId),查询的购物车中的当前购物项数据
        LambdaQueryWrapper<ShoppingCart> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(ShoppingCart::getUserId, BaseContext.getCurrentId());
        queryWrapper.eq(shoppingCart.getDishId()!=null,ShoppingCart::getDishId,shoppingCart.getDishId());
        queryWrapper.eq(shoppingCart.getSetmealId()!=null,ShoppingCart::getSetmealId,shoppingCart.getSetmealId());
        ShoppingCart cartForMysql = super.getOne(queryWrapper);
        //2.数量-1的操作，判断number是否<=0 执行删除操作，如果number>0,修改操作
        cartForMysql.setNumber(cartForMysql.getNumber()-1);
        if(cartForMysql.getNumber()<=0){//当number减完 小于等于 0 ，就说明此时购物车没有这个购物项，删除
            super.removeById(cartForMysql.getId());
        }else{
            super.updateById(cartForMysql);
        }

        return cartForMysql;
    }

}
