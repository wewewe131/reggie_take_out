package com.reggie.Controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.reggie.Common.R;
import com.reggie.Entity.Dish;
import com.reggie.Entity.DishFlavor;
import com.reggie.Service.CategoryService;
import com.reggie.Service.DishFlavorService;
import com.reggie.Service.DishService;
import com.reggie.dto.DishDto;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@Slf4j
@RequestMapping("/dish")
public class DishController {
    private final DishService dishService;
    private final DishFlavorService dishFlavorService;
    private final CategoryService categoryService;

    @Autowired
    public DishController(DishService dishService, DishFlavorService dishFlavorService, CategoryService categoryService) {
        this.dishService = dishService;
        this.dishFlavorService = dishFlavorService;
        this.categoryService = categoryService;
    }

    @PostMapping
    public R<String> save(@RequestBody DishDto dishDto) {
        log.info(dishDto.toString());

        dishService.saveWithFlavor(dishDto);

        return R.success("新增菜品成功");
    }

    @GetMapping("/page")
    public R<Page> page(int page, int pageSize, String name) {

        Page<Dish> pageInfo = new Page<>(page, pageSize);
        Page<DishDto> dtoPage = new Page<>(page, pageSize);

        LambdaQueryWrapper<Dish> lambdaQueryWrapper = new LambdaQueryWrapper<>();

        lambdaQueryWrapper.like(name != null, Dish::getName, name);

        lambdaQueryWrapper.orderByDesc(Dish::getUpdateTime);

        dishService.page(pageInfo, lambdaQueryWrapper);

        //对象拷贝 参数1：原对象 参数2：目标对象 参数3：不拷贝的属性
        BeanUtils.copyProperties(pageInfo, dtoPage, "records");

        List<Dish> dishes = pageInfo.getRecords();
        //将查询到的dish对象转换为dto对象，添加categoryName属性
        List<DishDto> dishDtos = dishes.stream().map(dish -> {
            DishDto dishDto = new DishDto();
            BeanUtils.copyProperties(dish, dishDto);
            String categoryName = categoryService.getById(dish.getCategoryId()).getName();
            dishDto.setCategoryName(categoryName);
            return dishDto;
        }).collect(Collectors.toList());

        dtoPage.setRecords(dishDtos);

        return R.success(dtoPage);

    }

    /**
     * 根据id查询菜品信息和对应的口味信息
     *
     * @param id
     * @return
     */
    @GetMapping("/{id}")
    public R<DishDto> dishDtoR(@PathVariable Long id) {

        DishDto byIdWithFlavor = dishService.getByIdWithFlavor(id);


        return R.success(byIdWithFlavor);
    }

    @PutMapping
    public R<String> update(@RequestBody DishDto dishDto) {
        log.info(dishDto.toString());
        dishService.updateWithFlavor(dishDto);
        return R.success("更新菜品成功");
    }

//    @GetMapping("/list")
//    public R<List<Dish>> list(Dish dish) {
//
//        LambdaQueryWrapper<Dish> lambdaQueryWrapper = new LambdaQueryWrapper<>();
//
//        lambdaQueryWrapper.eq(dish.getCategoryId() != null, Dish::getCategoryId, dish.getCategoryId());
//        lambdaQueryWrapper.orderByAsc(Dish::getId).orderByDesc(Dish::getUpdateTime);
//
//        List<Dish> list = dishService.list(lambdaQueryWrapper);
//
//        return R.success(list);
//    }

    @GetMapping("/list")
    public R<List<DishDto>> list(Dish dish) {

        LambdaQueryWrapper<Dish> lambdaQueryWrapper = new LambdaQueryWrapper<>();

        lambdaQueryWrapper.eq(dish.getCategoryId() != null, Dish::getCategoryId, dish.getCategoryId());
        lambdaQueryWrapper.orderByAsc(Dish::getId).orderByDesc(Dish::getUpdateTime);

        List<Dish> list = dishService.list(lambdaQueryWrapper);
        List<DishDto> dishDtos = list.stream().map(item -> {
            DishDto dishDto = new DishDto();
            BeanUtils.copyProperties(item, dishDto);
            Long categoryId = item.getCategoryId();
            String categoryName = categoryService.getById(categoryId).getName();
            dishDto.setCategoryName(categoryName);

            Long id = item.getId();
            LambdaQueryWrapper<DishFlavor> queryWrapper = new LambdaQueryWrapper<>();
            queryWrapper.eq(DishFlavor::getDishId, id);
            List<DishFlavor> dishFlavors = dishFlavorService.list(queryWrapper);
            dishDto.setFlavors(dishFlavors);

            return dishDto;
        }).collect(Collectors.toList());

        return R.success(dishDtos);
    }
}
