package com.reggie.Mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.reggie.Entity.Orders;
import org.apache.ibatis.annotations.Mapper;

/*
    这个接口专门操作mysql中employee表的接口
    因为我们现在使用了mybatisPlus所以，我们可以继承mybatisplus提供的接口，直接拥有了mybatisplus提供的增删改查的操作
 */
@Mapper
public interface OrdersMapper extends BaseMapper<Orders> {

}
