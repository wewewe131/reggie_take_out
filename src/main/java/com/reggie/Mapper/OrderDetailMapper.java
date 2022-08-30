package com.reggie.Mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.reggie.Entity.OrderDetail;
import org.apache.ibatis.annotations.Mapper;

/*
    因为我们现在使用了mybatisPlus所以，我们可以继承mybatisplus提供的接口，直接拥有了mybatisplus提供的增删改查的操作
 */
@Mapper
public interface OrderDetailMapper extends BaseMapper<OrderDetail> {

}
