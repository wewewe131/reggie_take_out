package com.reggie.Service.Impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.reggie.Common.CustomException;
import com.reggie.Entity.Category;
import com.reggie.Entity.Dish;
import com.reggie.Entity.Setmeal;
import com.reggie.Mapper.CategoryMapper;
import com.reggie.Service.CategoryService;
import com.reggie.Service.DishService;
import com.reggie.Service.SetmealService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class CategoryServiceImpl extends ServiceImpl<CategoryMapper, Category> implements CategoryService {

    private final DishService dishService;
    private final SetmealService setmealService;
    @Autowired
    public CategoryServiceImpl(SetmealService setmealService, DishService dishService) {
        this.setmealService = setmealService;
        this.dishService = dishService;
    }

    /**
     * 根据id删除一个分类
     * @param id
     */

    @Override
    public void remove(Long id) {
        //查询当前分类是否关联了菜品，如已关联则抛出一个异常

        LambdaQueryWrapper<Dish> lambdaQueryWrapper = new LambdaQueryWrapper<>();
        //添加查询条件
        lambdaQueryWrapper.eq(Dish::getCategoryId, id);
        int count = dishService.count(lambdaQueryWrapper);
        if (count >0){
            //抛出一个异常
            throw new CustomException("当前分类下关联了菜品，无法删除");
        }

        //查询当前分类是否关联了套餐，如已关联则抛出一个异常

        LambdaQueryWrapper<Setmeal> setmealLambdaQueryWrapper = new LambdaQueryWrapper<>();
        setmealLambdaQueryWrapper.eq(Setmeal::getCategoryId, id);
        int count2 = setmealService.count(setmealLambdaQueryWrapper);

        if (count2>0){
            //抛出一个异常
            throw new CustomException("当前分类下关联了套餐，无法删除");

        }


        //正常删除一个分类
        super.removeById(id);

    }
}
