package com.reggie.Service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.reggie.Entity.ShoppingCart;

public interface ShoppingCartService extends IService<ShoppingCart> {

    ShoppingCart saveShoppingCart(ShoppingCart shoppingCart);

    ShoppingCart sub(ShoppingCart shoppingCart);

}
