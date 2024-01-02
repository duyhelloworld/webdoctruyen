package com.duyhelloworld.controller;

import java.io.IOException;
import java.nio.file.FileAlreadyExistsException;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
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
import com.duyhelloworld.entity.Book;
import com.duyhelloworld.entity.Chapter;
import com.duyhelloworld.exception.AppException;
import com.duyhelloworld.model.ChapterModel;
import com.duyhelloworld.repository.BookRepository;
import com.duyhelloworld.repository.ChapterRepository;
import com.duyhelloworld.repository.CommentRepository;
import com.duyhelloworld.service.file.FileService;


@RestController
@RequestMapping("api/chapter")
public class ChapterController {

    @Autowired
    private FileService fileService;

    @Autowired
    private ChapterRepository chapterRepository;

    @Autowired
    private BookRepository bookRepository;

    @Autowired
    private CommentRepository commentRepository;

    @Autowired
    private ObjectMapper mapper;

    @Transactional
    @GetMapping("all")
    public List<ChapterModel> getAll(@RequestParam(required = false) Integer page) {
        if (page == null) {
            page = 1;
        }
        return chapterRepository.findAll(
            PageRequest.of(page-1, 10)
            .withSort(Sort.by("uploadAt")))
        .getContent().stream()
        .map(ChapterModel::convert)
        .collect(Collectors.toList());
    }

    @GetMapping("{id}")
    @Transactional
    public ResponseEntity<ChapterModel> getById(@PathVariable Integer id) {
        Chapter chapter = chapterRepository.findById(id)
                .orElseThrow(() -> new AppException(
                    HttpStatus.NOT_FOUND, 
                    "Không có chương nào có mã : " + id));
        Integer numberImage = fileService.getNumberImageOfChapter(chapter.getBook().getTitle(), 
            chapter.getFolderName());
        ChapterModel converted = ChapterModel.convert(chapter, numberImage);
        converted.setTitle(chapter.getTitle());
        converted.setNumberOfImage(
            fileService.getNumberImageOfChapter(chapter.getBook().getTitle(), 
                chapter.getFolderName()));
        return ResponseEntity.ok(converted);
    }

    @GetMapping
    @Transactional
	public ResponseEntity<Resource> loadChapter(
		@RequestParam Integer chapterId,
		@RequestParam Integer fileId) {
	    Chapter chapter = chapterRepository.findById(chapterId)
				.orElseThrow(() -> new AppException(
					HttpStatus.NOT_FOUND, 
					"Không có chương nào có mã : " + chapterId));
		Resource resource = fileService.getChapter(
			chapter.getBook().getTitle(), chapter.getFolderName(), fileId);
        if (resource == null) {
            throw new AppException(HttpStatus.NOT_FOUND, 
                "Không tìm thấy file " + chapter.getFolderName() + " : " + fileId);
        }
		return ResponseEntity.ok()
			.contentType(MediaType.valueOf("image/jpg"))
			.body(resource);
	}

    @PostMapping
    public ResponseEntity<ChapterModel> create(
        @RequestPart("model") String model,
        @RequestPart("files") List<MultipartFile> files) {

        ChapterModel chapterModel = null;
        try {
            chapterModel = mapper.readValue(model, ChapterModel.class);
        } catch (Exception e) {
            throw new AppException(HttpStatus.BAD_REQUEST, 
            "Sách sai định dạng");
        }
        if (chapterModel.getBookId() == null) {
            throw new AppException(HttpStatus.BAD_REQUEST, 
                "Không tìm thấy id của sách");
        }
        if (!StringUtils.hasText(chapterModel.getTitle())) {
            throw new AppException(HttpStatus.BAD_REQUEST, 
                "Tên chương không được để trống");
        }
        if (chapterRepository.isExistChapter(chapterModel.getBookId(), chapterModel.getTitle())
            .isPresent()) {
            throw new AppException(HttpStatus.BAD_REQUEST, 
                "Chương này đã tồn tại");
        }
        Chapter response = null;
        int numberImage = 0;
        try {
            Book book = bookRepository.findById(chapterModel.getBookId())
            .orElseThrow(() -> new AppException(HttpStatus.NOT_FOUND, 
                "Không tìm thấy sách này"));
            Chapter chapter = ChapterModel.convert(chapterModel);

            // Bỏ qua bookTitle khi truyền lên
            chapter.setBook(book);
            chapter.setUploadAt(LocalDateTime.now());

            Integer currentChapter = chapterRepository
                .getMaxChapterNumber(chapterModel.getBookId());
            chapter.setFolderName("Chapter_" + (currentChapter + 1));
            numberImage = fileService.saveChapter(files, book.getTitle(), chapter.getFolderName());
            response = chapterRepository.save(chapter);
        } catch (FileAlreadyExistsException e) {
            throw new AppException(HttpStatus.BAD_REQUEST, 
                    "Chương này đã tồn tại. Hãy thử lại với tên chương khác");
        } catch (IOException e) {
            throw new AppException(HttpStatus.BAD_REQUEST, 
                "Lỗi khi lưu file");
        }
        return ResponseEntity.ok(ChapterModel.convert(response, numberImage));
    }

    @PutMapping("{id}")
    public ResponseEntity<ChapterModel> update(
        @PathVariable Integer id,
        @RequestPart("model") String model,
        @RequestPart("files") List<MultipartFile> files) {

        ChapterModel chapterModel = null;
        try {
            chapterModel = mapper.readValue(model, ChapterModel.class);
            chapterModel.setTitle(chapterModel.getTitle().replace(" ", "_"));
        } catch (Exception e) {
            throw new AppException(HttpStatus.BAD_REQUEST,
                "Sách sai định dạng");
        }
        Chapter chapter = chapterRepository.isExistChapter(chapterModel.getBookId(), chapterModel.getTitle())
            .orElseThrow(() -> new AppException(HttpStatus.NOT_FOUND, 
                "Không tìm thấy chương này"));
        if (bookRepository.existsById(chapterModel.getBookId())) 
        {
            throw new AppException(HttpStatus.NOT_FOUND, 
                "Không tìm thấy sách này");
        }
        // Không cập nhật folderName
        chapter.setTitle(chapterModel.getTitle());
        chapter.setUploadAt(LocalDateTime.now());
        // fileService.saveChapter(files, book.getTitle(), chapter.getFolderName());
        Chapter response = chapterRepository.save(chapter);
        Integer numberImage = fileService.getNumberImageOfChapter(response.getBook().getTitle(), 
            response.getFolderName());
        return ResponseEntity.ok(ChapterModel.convert(response, numberImage));
    }

    @DeleteMapping("{id}")
    @Transactional
    public ResponseEntity<String> delete(@PathVariable Integer id) {
        Chapter chapter = chapterRepository.findById(id)
            .orElseThrow(() -> new AppException(HttpStatus.NOT_FOUND, 
                "Không tìm thấy chương này"));
        
        fileService.deleteChapter(chapter.getBook().getTitle(), chapter.getFolderName());
        commentRepository.deleteAll(chapter.getComments());
        chapterRepository.delete(chapter);
        return ResponseEntity.ok("Xóa thành công chương " + id
            + " của truyện " + chapter.getBook().getTitle());
    }
}
