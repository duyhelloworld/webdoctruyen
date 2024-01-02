package com.duyhelloworld.model;

import lombok.Data;
import com.duyhelloworld.entity.Category;

@Data
public class CategoryModel {
    private Integer id;
    private String name;
    private String description;
    private Integer numberOfBook;

    public static CategoryModel convert(Category category) {
        CategoryModel categoryModel = new CategoryModel();
        categoryModel.setId(category.getId());
        categoryModel.setName(category.getName());
        categoryModel.setDescription(category.getDescription());
        categoryModel.setNumberOfBook(category.getBooks().size());
        return categoryModel;
    }

    public static Category convert(CategoryModel categoryModel) {
        Category category = new Category();
        category.setId(categoryModel.getId());
        category.setName(categoryModel.getName());
        category.setDescription(categoryModel.getDescription());
        return category;
    }
}
