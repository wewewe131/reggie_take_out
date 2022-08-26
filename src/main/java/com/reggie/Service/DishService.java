package com.reggie.Service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.reggie.Entity.Dish;
import com.reggie.dto.DishDto;

public interface DishService extends IService<Dish> {
    public void saveWithFlavor(DishDto dishDto);

    public DishDto getByIdWithFlavor(Long id);

    public void updateWithFlavor(DishDto dishDto);
}
