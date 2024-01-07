package com.duyhelloworld.service;

import java.util.List;

import com.duyhelloworld.model.CategoryModel;

public interface CategoryService {
    public List<CategoryModel> getAll(Integer page);

    public CategoryModel getById(Integer id);

    public CategoryModel create(CategoryModel categoryModel);

    public CategoryModel delete(Integer categoryId);
}
