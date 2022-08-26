package com.reggie.Service.Impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.reggie.Common.CustomException;
import com.reggie.Entity.Setmeal;
import com.reggie.Entity.SetmealDish;
import com.reggie.Mapper.SetmealMapper;
import com.reggie.Service.SetmealDishService;
import com.reggie.Service.SetmealService;
import com.reggie.dto.SetmealDto;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Slf4j
public class SetmealServiceImpl extends ServiceImpl<SetmealMapper, Setmeal> implements SetmealService {


    private final SetmealDishService setmealDishService;
    @Autowired
    public SetmealServiceImpl(SetmealDishService setmealDishService) {
        this.setmealDishService = setmealDishService;
    }

    @Transactional
    public void saveWithDish(SetmealDto setmealDto) {
        this.save(setmealDto);

        List<SetmealDish> dishes = setmealDto.getSetmealDishes();

        List<SetmealDish> collect = dishes.stream().map(setmealDish -> {
            setmealDish.setSetmealId(setmealDto.getId());
            return setmealDish;
        }).collect(Collectors.toList());


        setmealDishService.saveBatch(collect);


    }

    @Override
    public void removeWithDish(List<Long> ids) {
        LambdaQueryWrapper<Setmeal> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.in(Setmeal::getId, ids);
        queryWrapper.eq(Setmeal::getStatus, 1);

        int count = this.count(queryWrapper);
        if (count > 0) {
            throw new CustomException("该套餐已经被使用，不能删除");
        }

        this.removeByIds(ids);

        LambdaQueryWrapper<SetmealDish> queryWrappe = new LambdaQueryWrapper<>();

        queryWrappe.in(SetmealDish::getSetmealId, ids);
        setmealDishService.remove(queryWrappe);
    }
}

