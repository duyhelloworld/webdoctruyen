package com.duyhelloworld.controller;

import java.util.List;

import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import jakarta.transaction.Transactional;
import com.duyhelloworld.model.CategoryModel;
import com.duyhelloworld.service.CategoryService;

@RestController
@RequestMapping("api/category")
public class CategoryController {

    private CategoryService categoryService;

    public CategoryController(CategoryService categoryService) {
        this.categoryService = categoryService;
    }

    @GetMapping("all")
    @Transactional
    public List<CategoryModel> getAll(
        @RequestParam(required = false) Integer page) {
        return categoryService.getAll(page);
    }

    @GetMapping("/{id}")
    @Transactional
    public CategoryModel getById(@PathVariable Integer id) {
        return categoryService.getById(id);
    }

    @PostMapping
    @Transactional
    public String create(@RequestBody CategoryModel categoryModel) {
        return "Đã tạo thành công thể loại. Mã mới là " + categoryService.create(categoryModel).getId();
    }

    @DeleteMapping("{id}")
    @Transactional
    public String delete(@PathVariable Integer id) {
        return "Đã xóa thành công thể loại " + categoryService.delete(id).getName();
    }
}
