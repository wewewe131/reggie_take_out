package com.reggie.Mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.reggie.Entity.ShoppingCart;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface ShoppingCartMapper extends BaseMapper<ShoppingCart> {
}
