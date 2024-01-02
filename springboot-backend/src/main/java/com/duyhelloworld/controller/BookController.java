package com.duyhelloworld.controller;

import com.duyhelloworld.entity.Book;
import com.duyhelloworld.entity.Category;
import com.duyhelloworld.exception.AppException;
import com.duyhelloworld.model.BookModel;
import com.duyhelloworld.repository.BookRepository;
import com.duyhelloworld.repository.CategoryRepository;
import com.duyhelloworld.repository.ChapterRepository;
import com.duyhelloworld.repository.CommentRepository;
import com.duyhelloworld.repository.RatingRepository;
import com.duyhelloworld.service.file.FileService;
import com.duyhelloworld.util.LanguageConverter;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.util.StringUtils;

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

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("api/book")
public class BookController {

	@Autowired
	private FileService fileService;

	@Autowired
	private ObjectMapper mapper;

	@Autowired
	private BookRepository bookRepository;

	@Autowired
	private CategoryRepository categoryRepository;

	@Autowired
	private ChapterRepository chapterRepository;

	@Autowired
	private RatingRepository ratingRepository;

	@Autowired
	private CommentRepository commentRepository;

	private final int PAGE_SIZE = 12;

	@Transactional
	@GetMapping("all")
	public List<BookModel> GetAll(@RequestParam(required = false) Integer page){
		if (page == null)
			page = 1;
		return bookRepository.findAll(
			PageRequest.of(page-1, PAGE_SIZE))
			.getContent().stream()
			.map(b -> BookModel.convert(b))
			.collect(Collectors.toList());
	}

	@Transactional
	@GetMapping("{id}")
	public ResponseEntity<BookModel> getById(@PathVariable Integer id) {	
		Book book = bookRepository.findById(id)
				.orElseThrow(() -> new AppException(
					HttpStatus.NOT_FOUND, 
					"Không có sách nào có mã : " + id));
		return ResponseEntity.ok(BookModel.convert(book));
	}

	@GetMapping
	@Transactional
	public ResponseEntity<List<BookModel>> getBookByName(
			@RequestParam String keyword) {
		// StringUtil check ko null và có kí tự 
		if (!StringUtils.hasLength(keyword))
			throw new AppException(HttpStatus.BAD_REQUEST, 
				"Tên sách không được để trống");
		List<Book> result = bookRepository.search(keyword);
		return ResponseEntity.ok(result.stream()
			.map(b -> BookModel.convert(b))
			.collect(Collectors.toList()));
	}

	@GetMapping("category")
	@Transactional
	public ResponseEntity<List<BookModel>> getBookByCategory(
			@RequestParam String name) {
		Category category = categoryRepository.findByName(name)
			.orElseThrow(() -> new AppException(HttpStatus.NOT_FOUND, 
				"Không có thể loại nào tên : " + name));
		return ResponseEntity.ok(category.getBooks().stream()
			.map(b -> BookModel.convert(b))
			.collect(Collectors.toList()));
	}

	@GetMapping("cover-image")
	public ResponseEntity<Resource> getCoverImage(
			@RequestParam(required = false) String filename) {
		if (filename == null)
			filename = "default-coverimage.png";
		Resource resource = fileService.getCoverImage(filename);
		return ResponseEntity.ok()
			.contentType(MediaType.IMAGE_PNG)
			.body(resource);
	}

	@PostMapping
	public String addBook(
		@RequestPart String model,
		@RequestPart(required = false) MultipartFile coverImage) 
	{
		BookModel bookModel = null;
		try {
			bookModel = mapper.readValue(model, BookModel.class);
		} catch (Exception e) {
			throw new AppException(HttpStatus.BAD_REQUEST, 
				"Sách sai định dạng");
		}

		if (bookModel.getReleaseDate().isAfter(LocalDate.now()))
			throw new AppException(HttpStatus.BAD_REQUEST, 
				"Ngày phát hành không được lớn hơn ngày hiện tại");
		if (bookRepository.existsByTitle(bookModel.getTitle()))
			throw new AppException(HttpStatus.CONFLICT, 
				"Đã tồn tại sách có tên : " + bookModel.getTitle() + " trong hệ thống");
		Book book = new Book();

		String folderName = LanguageConverter.convert(bookModel.getTitle().replace(" ", "_"));
		if (!fileService.createNewBook(folderName)) {
			throw new AppException(HttpStatus.CONFLICT, "Đã tồn tại sách này");
		}
		book.setTitle(folderName);
		book.setReleaseDate(bookModel.getReleaseDate());
		book.setAuthor(bookModel.getAuthor());
		book.setDescription(bookModel.getDescription());

		book.setUploadAt(LocalDateTime.now());
		if (coverImage != null) {
			fileService.saveCoverImage(coverImage);
			book.setCoverImage(coverImage.getOriginalFilename());
		} else {
			book.setCoverImage("default-coverimage.png");
		}
		bookRepository.save(book);
		return "Thêm thành công sách mới. Mã sách : " + book.getId();
	}
	
	@Transactional
	@PutMapping("{id}")
	public ResponseEntity<BookModel> editBookInformation(
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

		Book book = bookRepository.findById(id)
				.orElseThrow(() -> new AppException(
					HttpStatus.NOT_FOUND, 
					"Không có sách nào có mã : " + id));
		if (bookModel.getReleaseDate().isAfter(LocalDate.now()))
			throw new AppException(HttpStatus.BAD_REQUEST, 
				"Ngày phát hành không được lớn hơn ngày hiện tại");
		if (bookRepository.existsByTitle(bookModel.getTitle()))
			throw new AppException(HttpStatus.CONFLICT, 
				"Đã tồn tại sách có tên : " + bookModel.getTitle() + " trong hệ thống");
		book.setTitle(bookModel.getTitle().replace(" ", "_"));
		book.setAuthor(bookModel.getAuthor());
		book.setReleaseDate(bookModel.getReleaseDate());
		book.setDescription(bookModel.getDescription());
				
		List<Category> categories = categoryRepository.findByName(bookModel.getCategories());
		book.setCategories(categories);

		if (coverImage != null) {
			fileService.saveCoverImage(coverImage);
			book.setCoverImage(coverImage.getOriginalFilename());
		} else {
			book.setCoverImage("default-coverimage.png");
		}

		Book updatedBook = bookRepository.save(book);
		return ResponseEntity.ok(BookModel.convert(updatedBook));
	}
	
	@Transactional
	@DeleteMapping("{id}")
	public ResponseEntity<String> delete(@PathVariable Integer id) {
		Book book = bookRepository.findById(id)
				.orElseThrow(() -> new AppException(HttpStatus.NOT_FOUND,
				"Không có sách nào mã " + id));
		categoryRepository.deleteAll(book.getCategories());
		commentRepository.deleteAll(book.getChapters().stream()
			.flatMap(chapter -> chapter.getComments().stream())
			.collect(Collectors.toList()));
		chapterRepository.deleteAll(book.getChapters());
		ratingRepository.deleteAll(book.getRatings());
		bookRepository.delete(book);
		fileService.deleteCoverImage(book.getCoverImage());
		fileService.deleteBook(book.getTitle().replace(" ", "_"));
		return ResponseEntity.ok("Xóa thành công sách có mã " + id);
	}
}
