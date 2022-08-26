package com.reggie.Service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.reggie.Entity.Setmeal;
import com.reggie.dto.SetmealDto;

import java.util.List;

public interface SetmealService extends IService<Setmeal> {

    public void saveWithDish(SetmealDto setmealDto);

    void removeWithDish(List<Long> ids);
}

