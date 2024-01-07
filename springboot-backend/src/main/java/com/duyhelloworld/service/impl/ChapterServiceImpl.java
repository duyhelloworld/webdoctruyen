package com.duyhelloworld.service.impl;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.core.io.Resource;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import com.duyhelloworld.entity.Book;
import com.duyhelloworld.entity.Chapter;
import com.duyhelloworld.exception.AppException;
import com.duyhelloworld.model.ChapterModel;
import com.duyhelloworld.repository.BookRepository;
import com.duyhelloworld.repository.ChapterRepository;
import com.duyhelloworld.repository.CommentRepository;
import com.duyhelloworld.service.ChapterService;
import com.duyhelloworld.service.FileService;

import lombok.AllArgsConstructor;

@Service
@AllArgsConstructor
public class ChapterServiceImpl implements ChapterService {

    private ChapterRepository chapterRepository;

    private BookRepository bookRepository;

    private CommentRepository commentRepository;

    private FileService fileService;

    @Override
    public List<ChapterModel> getAll(Integer page) {
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

    @Override
    public ChapterModel getById(Integer id) {
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
        return converted;
    }

    @Override
    public Resource loadChapter(Integer chapterId, Integer fileId) {
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
        return resource;
    }

    @Override
    public ChapterModel create(ChapterModel chapterModel, List<MultipartFile> files) {
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
        fileService.saveChapter(files, book.getTitle(), chapter.getFolderName());
        response = chapterRepository.save(chapter);
        return ChapterModel.convert(response, numberImage);
    }

    @Override
    public ChapterModel update(Integer id, ChapterModel chapterModel, List<MultipartFile> files) {
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
        return ChapterModel.convert(response, numberImage);
    }

    @Override
    public ChapterModel delete(Integer id) {
        Chapter chapter = chapterRepository.findById(id)
            .orElseThrow(() -> new AppException(HttpStatus.NOT_FOUND, 
                "Không tìm thấy chương này"));
        
        fileService.deleteChapter(chapter.getBook().getTitle(), chapter.getFolderName());
        commentRepository.deleteAll(chapter.getComments());
        chapterRepository.delete(chapter);
        return ChapterModel.convert(chapter);
    }
}
