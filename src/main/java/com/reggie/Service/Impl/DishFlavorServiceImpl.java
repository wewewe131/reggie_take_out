package com.reggie.Service.Impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.reggie.Entity.DishFlavor;
import com.reggie.Mapper.DishFlavorMapper;
import com.reggie.Service.DishFlavorService;
import org.springframework.stereotype.Service;

@Service
public class DishFlavorServiceImpl extends ServiceImpl<DishFlavorMapper, DishFlavor> implements DishFlavorService {
}
