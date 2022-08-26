package com.reggie.dto;


import com.reggie.Entity.Setmeal;
import com.reggie.Entity.SetmealDish;
import lombok.Data;
import java.util.List;

@Data
public class SetmealDto extends Setmeal {

    private List<SetmealDish> setmealDishes;

    private String categoryName;
}
