package com.reggie.Controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.reggie.Common.R;
import com.reggie.Entity.Category;
import com.reggie.Service.CategoryService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static jdk.nashorn.internal.runtime.regexp.joni.Config.log;

/**
 * 分类控制器
 */
@Slf4j
@RestController
@RequestMapping("/category")
public class CategoryController {
    private final CategoryService categoryService;

    @Autowired
    public CategoryController(CategoryService categoryService) {
        this.categoryService = categoryService;
    }

    @PostMapping
    public R<String> save(@RequestBody Category category) {
        log.info("接收到的分类信息：{}", category.toString());
        categoryService.save(category);
        return R.success("新增分类成功");
    }

    @GetMapping("/page")
    public R<Page> page(int page, int pageSize) {
        //分页构造器
        Page<Category> pageInfo = new Page<>(page, pageSize);
        //条件分页查询
        LambdaQueryWrapper<Category> wrapper = new LambdaQueryWrapper<>();
        //添加排序条件
        wrapper.orderByAsc(Category::getSort);
        //查询
        categoryService.page(pageInfo,wrapper);
        return R.success(pageInfo);
    }

    /**
     * 根据id删除一个分类
     * @param id
     * @return
     */
    @DeleteMapping
    public R<String> delete(Long id){
        log.info("删除分类,id:{}",id);
//        categoryService.removeById(id);
        categoryService.remove(id);//自定义的删除方法
        return R.success("分类信息删除 成功");
    }

    /**
     * 根据id修改分类信息
     */
    @PutMapping
    public R<String> update(@RequestBody Category category){
        log.info("修改分类信息：{}",category.toString());
        categoryService.updateById(category);
        return R.success("修改分类信息成功");
    }
    @GetMapping("/list")
    public R<List<Category>> list(Category category){
        LambdaQueryWrapper<Category> categoryLambdaQueryWrapper = new LambdaQueryWrapper<>();

        categoryLambdaQueryWrapper.eq(category.getType() != null, Category::getType, category.getType());
        //如果sort相同则根据UpdateTime排序
        categoryLambdaQueryWrapper.orderByAsc(Category::getSort).orderByDesc(Category::getUpdateTime);

        List<Category> list = categoryService.list(categoryLambdaQueryWrapper);
        return R.success(list);
    }
}
