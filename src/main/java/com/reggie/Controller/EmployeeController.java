package com.reggie.Controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.reggie.Common.R;
import com.reggie.Entity.Employee;
import com.reggie.Service.EmployeeService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.DigestUtils;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.time.LocalDateTime;

import static com.reggie.Common.R.success;

@Slf4j
@RestController
@RequestMapping("/employee")
public class EmployeeController {
    private final EmployeeService employeeService;

    @Autowired
    public EmployeeController(EmployeeService employeeService) {
        this.employeeService = employeeService;
    }

    /**
     * 员工登录
     *
     * @param request
     * @param employee
     * @return
     */
    @PostMapping("/login")
    public R<Employee> login(HttpServletRequest request, @RequestBody Employee employee) {
        log.info("员工登录");
        //md5加密
        String password = employee.getPassword();
        password = DigestUtils.md5DigestAsHex(password.getBytes());

        //查询数据库
        LambdaQueryWrapper<Employee> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(Employee::getUsername, employee.getUsername());
        Employee emp = employeeService.getOne(queryWrapper);

        //判断用户是否存在
        if (emp == null) {
            return R.error("用户不存在");
        }
        //判断密码是否正确
        if (!password.equals(emp.getPassword())) {
            return R.error("密码错误");
        }

        //判断用户是否被禁用
        if (emp.getStatus() == 0) {
            return R.error("用户被禁用");
        }
        //登录成功，将用户id存入session域中
        request.getSession().setAttribute("employee", emp.getId());
        return success(emp);
    }

    /**
     * 登出
     *
     * @param request
     * @return
     */
    @PostMapping("/logout")
    public R<String> logout(HttpServletRequest request) {
        request.getSession().removeAttribute("employee");
        return success("退出成功");
    }

    /**
     * @param request
     * @param employee
     * @return
     */
    @PostMapping//使用post方式访问@RequestMapping("/employee")默认会使用这个方法
    public R<String> save(HttpServletRequest request, @RequestBody Employee employee) {
        log.info("新增员工信息:{}", employee.toString());
        String defaultPassword = "123456";
        employee.setPassword(DigestUtils.md5DigestAsHex(defaultPassword.getBytes()));
//        employee.setCreateTime(LocalDateTime.now()/**获取本地当前时间*/);
//        employee.setUpdateTime(LocalDateTime.now());
//        //获取当前登录用户的ID
//        Long empid = (Long) request.getSession().getAttribute("employee");
//
//        employee.setUpdateUser(empid);
//        employee.setCreateUser(empid); 使用mybatis plus公共字段填充 MyMetaObjectHandler 替换这条代码
        employeeService.save(employee);
        return success("新增员工成功");
    }

    /**
     * 分页查询员工信息
     *
     * @param page
     * @param pageSize
     * @param name
     * @return
     */
    @GetMapping("/page")
    public R<Page> page(int page, int pageSize, String name) {
        log.info("page={},pageSize={},name={}", page, pageSize, name);
        //构造分页构造器
        Page pageinfo = new Page(page, pageSize);
        //构造查询条件
        LambdaQueryWrapper<Employee> queryWrapper = new LambdaQueryWrapper<>();
        //like(一个Boolean值,要查询的字段,查询的值) 如果Boolean值为true则添加查询条件，为false则不添加查询条件
        queryWrapper.like(StringUtils.isNotEmpty(name), Employee::getName, name);
        //添加排序条件
        queryWrapper.orderByDesc(Employee::getUpdateTime);
        //查询数据
        employeeService.page(pageinfo, queryWrapper);//使用MybatisPlus提供的分页查询方法
        return R.success(pageinfo);
    }

    /**
     *
     * @param httpServletRequest
     * @param employee
     * @return
     */
    @PutMapping
    public R<String> update(HttpServletRequest httpServletRequest,@RequestBody Employee employee) {
        log.info("修改员工信息:{}", employee.toString());
        long threadId = Thread.currentThread().getId();
        log.info("线程ID：{}",threadId);

//        employee.setUpdateTime(LocalDateTime.now()); 使用mybatis plus公共字段填充替换这条代码

        //获取当前登录用户的ID
//        Long empid = (Long) httpServletRequest.getSession().getAttribute("employee");
//        employee.setUpdateUser(empid);    使用mybatis plus公共字段填充替换这条代码
        employeeService.updateById(employee);
        return success("员工信息修改成功");
    }

    /**
     * 根据id获取用户
     * @param id
     * @return
     */
    @GetMapping("/{id}")
    public R<Employee> get(@PathVariable Long id) {
        log.info("查询员工信息:{}", id);
        if (id == null) {
            return R.error("没有查询到对应员工");
        }
        return success(employeeService.getById(id));
    }

}
