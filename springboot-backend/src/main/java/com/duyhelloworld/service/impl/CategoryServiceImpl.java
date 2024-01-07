package com.duyhelloworld.service.impl;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import com.duyhelloworld.configuration.AppConstant;
import com.duyhelloworld.entity.Category;
import com.duyhelloworld.exception.AppException;
import com.duyhelloworld.model.CategoryModel;
import com.duyhelloworld.repository.BookRepository;
import com.duyhelloworld.repository.CategoryRepository;
import com.duyhelloworld.service.CategoryService;

import lombok.AllArgsConstructor;

@Service
@AllArgsConstructor
public class CategoryServiceImpl implements CategoryService {

    private CategoryRepository categoryRepository;

    private BookRepository bookRepository;

    @Override
    public List<CategoryModel> getAll(Integer page) {
        if (page == null) {
                page = 1;
            }
        return categoryRepository.findAll(
            PageRequest.of(page - 1, AppConstant.PAGE_SIZE)).getContent().stream()
            .map(CategoryModel::convert)
            .collect(Collectors.toList());
    }

    @Override
    public CategoryModel getById(Integer id) {
        Category category = categoryRepository.findById(id)
            .orElseThrow(() -> new AppException(HttpStatus.NOT_FOUND, "Không thấy thể loại này"));
        return CategoryModel.convert(category);
    }

    @Override
    public CategoryModel create(CategoryModel categoryModel) {
        Category category = CategoryModel.convert(categoryModel);
        if (categoryRepository.existsByName(category.getName())) {
            throw new AppException(HttpStatus.BAD_REQUEST,
             "Thể loại này đã tồn tại. Thử thể loại khác");
        }
        categoryRepository.save(category);
        return CategoryModel.convert(category);
    }

    @Override
    public CategoryModel delete(Integer categoryId) {
        Category category = categoryRepository.findById(categoryId)
            .orElseThrow(() -> new AppException(HttpStatus.NOT_FOUND, 
                "Không thấy thể loại này"));
        bookRepository.deleteAll(category.getBooks());
        categoryRepository.delete(category);
        return CategoryModel.convert(category);
    }
}
