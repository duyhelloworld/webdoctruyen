package com.duyhelloworld.service.impl;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.core.io.Resource;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import com.duyhelloworld.configuration.AppConstant;
import com.duyhelloworld.entity.Book;
import com.duyhelloworld.entity.Category;
import com.duyhelloworld.exception.AppException;
import com.duyhelloworld.model.BookModel;
import com.duyhelloworld.repository.BookRepository;
import com.duyhelloworld.repository.CategoryRepository;
import com.duyhelloworld.repository.ChapterRepository;
import com.duyhelloworld.repository.CommentRepository;
import com.duyhelloworld.repository.RatingRepository;
import com.duyhelloworld.service.BookService;
import com.duyhelloworld.service.FileService;
import com.duyhelloworld.util.LanguageConverter;

import lombok.AllArgsConstructor;

@Service
@AllArgsConstructor
public class BookServiceImpl implements BookService {
    private FileService fileService;
    
	private BookRepository bookRepository;

	private CategoryRepository categoryRepository;

	private ChapterRepository chapterRepository;

	private RatingRepository ratingRepository;

	private CommentRepository commentRepository;

	@Override
	public List<BookModel> getAll(Integer page) {
		if (page == null)
			page = 1;
		return bookRepository.findAll(
			PageRequest.of(page-1, AppConstant.PAGE_SIZE))
			.getContent().stream()
			.map(b -> BookModel.convert(b))
			.collect(Collectors.toList());
	}

	@Override
	public BookModel getById(Integer id) {
		Book book = bookRepository.findById(id)
				.orElseThrow(() -> new AppException(
					HttpStatus.NOT_FOUND, 
					"Không có sách nào có mã : " + id));
		return BookModel.convert(book);
	}

	@Override
	public List<BookModel> search(String keyword) {
		if (!StringUtils.hasLength(keyword))
			throw new AppException(HttpStatus.BAD_REQUEST, 
				"Tên sách không được để trống");
		List<Book> result = bookRepository.search(keyword);
		return result.stream()
			.map(b -> BookModel.convert(b))
			.collect(Collectors.toList());
	}

	@Override
	public List<BookModel> getByCategory(Integer categoryId) {
		Category category = categoryRepository.findById(categoryId)
			.orElseThrow(() -> new AppException(HttpStatus.NOT_FOUND, 
				"Không có thể loại nào có mã : " + categoryId));
		return category.getBooks().stream()
			.map(b -> BookModel.convert(b))
			.collect(Collectors.toList());
	}

	@Override
	public Resource getCoverImage(Integer id) {
		Book book = bookRepository.findById(id)
			.orElseThrow(() -> new AppException(HttpStatus.NOT_FOUND, 
				"Không có sách nào có mã : " + id));
		String filename = book.getCoverImage();
		if (book.getCoverImage() == null)
			filename = "default-coverimage.png";
		return fileService.getCoverImage(filename);
	}

	@Override
	public BookModel addBook(BookModel bookModel, MultipartFile coverImage) {
		if (bookModel.getReleaseDate().isAfter(LocalDate.now()))
			throw new AppException(HttpStatus.BAD_REQUEST, 
				"Ngày phát hành không được lớn hơn ngày hiện tại");
		if (bookRepository.existsByTitle(bookModel.getTitle()))
			throw new AppException(HttpStatus.CONFLICT, 
				"Đã tồn tại sách có tên : " + bookModel.getTitle() + " trong hệ thống");
		Book book = new Book();

		String folderName = LanguageConverter.convert(bookModel.getTitle().replace(" ", "_"));
		fileService.createNewBook(folderName);
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
		return BookModel.convert(book);
	}

	@Override
	public BookModel editInformation(Integer id, BookModel bookModel, MultipartFile coverImage) {
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
		return BookModel.convert(updatedBook);
	}

	@Override
	public BookModel deleteBook(Integer id) {
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
		return BookModel.convert(book);
	}
}
