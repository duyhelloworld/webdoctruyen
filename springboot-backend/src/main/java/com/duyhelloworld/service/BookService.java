package com.duyhelloworld.service;

import java.util.List;

import org.springframework.core.io.Resource;
import org.springframework.web.multipart.MultipartFile;

import com.duyhelloworld.model.BookModel;

public interface BookService {
    public List<BookModel> getAll(Integer page);

    public BookModel getById(Integer id);

    public List<BookModel> search(String keyword);

    public List<BookModel> getByCategory(Integer categoryId);
    
    public Resource getCoverImage(Integer id);

    public BookModel addBook(BookModel bookModel, MultipartFile coverImage);

    public BookModel editInformation(Integer id, BookModel bookModel, MultipartFile coverImage);
    
    public BookModel deleteBook(Integer id);
}
