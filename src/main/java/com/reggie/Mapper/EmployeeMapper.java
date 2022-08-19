package com.reggie.Mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.reggie.Entity.Employee;
import org.apache.ibatis.annotations.Mapper;


@Mapper
public interface EmployeeMapper extends BaseMapper<Employee> {
}
