package com.reggie.Service.Impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.reggie.Entity.Dish;
import com.reggie.Entity.DishFlavor;
import com.reggie.Mapper.DishMapper;
import com.reggie.Service.DishFlavorService;
import com.reggie.Service.DishService;
import com.reggie.dto.DishDto;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Slf4j
public class DishServiceImpl extends ServiceImpl<DishMapper, Dish> implements DishService {

    private final DishFlavorService dishFlavorService;
    @Autowired
    public DishServiceImpl(DishFlavorService dishFlavorService) {
        this.dishFlavorService = dishFlavorService;
    }

    @Transactional
    public void saveWithFlavor(DishDto dishDto) {
        this.save(dishDto);
        Long id = dishDto.getId();
        List<DishFlavor> collect = dishDto.getFlavors().stream().map(item -> {
            item.setDishId(id);
            return item;
        }).collect(Collectors.toList());
        dishFlavorService.saveBatch(collect);
    }

    @Override
    public DishDto getByIdWithFlavor(Long id) {

        Dish dish = this.getById(id);

        DishDto dishDto = new DishDto();

        BeanUtils.copyProperties(dish, dishDto);

        LambdaQueryWrapper<DishFlavor> lambdaQueryWrapper = new LambdaQueryWrapper<>();
        lambdaQueryWrapper.eq(DishFlavor::getDishId, dish.getId());
        List<DishFlavor> dishFlavors = dishFlavorService.list(lambdaQueryWrapper);
        dishDto.setFlavors(dishFlavors);
        return dishDto;
    }

    @Override
    public void updateWithFlavor(DishDto dishDto) {
        this.updateById(dishDto);//更新Dish基本信息
        //删除当前菜品对应的口味数据
        LambdaQueryWrapper<DishFlavor> lambdaQueryWrapper = new LambdaQueryWrapper<>();
        lambdaQueryWrapper.eq(DishFlavor::getDishId, dishDto.getId());
        dishFlavorService.remove(lambdaQueryWrapper);

        //更新菜品口味数据
        List<DishFlavor> collect = dishDto.getFlavors().stream().map(item -> {
            item.setDishId(dishDto.getId());
            return item;
        }).collect(Collectors.toList());
        dishFlavorService.saveBatch(collect);


    }
}
