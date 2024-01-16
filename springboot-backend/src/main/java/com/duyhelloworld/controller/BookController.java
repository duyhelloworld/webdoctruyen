package com.duyhelloworld.controller;

import com.duyhelloworld.configuration.AppConstant;
import com.duyhelloworld.exception.AppException;
import com.duyhelloworld.model.BookModel;
import com.duyhelloworld.service.BookService;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.fasterxml.jackson.databind.ObjectMapper;

import jakarta.transaction.Transactional;

import java.util.List;

@RestController
@RequestMapping("api/book")

public class BookController {
	
	private ObjectMapper mapper;

	private BookService bookService;

	@Transactional
	@GetMapping("all")
	public List<BookModel> GetAll(@RequestParam(required = false) Integer page){
		return bookService.getAll(page);
	}

	@Transactional
	@GetMapping("{id}")
	public BookModel getById(@PathVariable Integer id) {	
		return bookService.getById(id);
	}

	@GetMapping
	@Transactional
	public List<BookModel> getBookByName(
			@RequestParam String keyword) {
		return bookService.search(keyword);
	}

	@GetMapping("category/{id}")
	@Transactional
	public List<BookModel> getBookByCategory(
			@PathVariable Integer id) {
		return bookService.getByCategory(id);
	}

	@GetMapping("cover-image/{id}")
	public ResponseEntity<Resource> getCoverImage(
		@PathVariable Integer id) {
		return ResponseEntity.ok()
			.contentType(AppConstant.USER_AVATAR_FILE_TYPE)
			.body(bookService.getCoverImage(id));
	}

	@PostMapping
	public String addBook(
		@RequestPart String model,
		@RequestPart(required = false) MultipartFile coverImage) {
		
		BookModel bookModel = null;
		try {
			bookModel = mapper.readValue(model, BookModel.class);
		} catch (Exception e) {
			throw new AppException(HttpStatus.BAD_REQUEST, 
				"Sách sai định dạng");
		}
		return "Thêm thành công sách : " + bookService.addBook(bookModel, coverImage).getTitle();
	}
	
	@Transactional
	@PutMapping("{id}")
	public BookModel editBookInformation(
		@PathVariable Integer id, 
		@RequestPart String model,
		@RequestPart(value = "coverimage", required = false) MultipartFile coverImage) {

		BookModel bookModel = null;
		try {
			bookModel = mapper.readValue(model, BookModel.class);
		} catch (Exception e) {
			throw new AppException(HttpStatus.BAD_REQUEST, 
				"Sách sai định dạng");
		}
		return bookService.editInformation(id, bookModel, coverImage);
	}
	
	@Transactional
	@DeleteMapping("{id}")
	public String delete(@PathVariable Integer id) {
		return "Xóa thành công sách có mã : " + bookService.deleteBook(id).getId();
	}
}
