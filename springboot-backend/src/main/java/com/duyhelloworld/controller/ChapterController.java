package com.duyhelloworld.controller;

import java.util.List;

import org.springframework.core.io.Resource;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
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
import com.duyhelloworld.exception.AppException;
import com.duyhelloworld.model.ChapterModel;
import com.duyhelloworld.service.ChapterService;

@RestController
@RequestMapping("api/chapter")
public class ChapterController {

    private ObjectMapper mapper;

    private ChapterService chapterService;

    @Transactional
    @GetMapping("all")
    public List<ChapterModel> getAll(@RequestParam(required = false) Integer page) {
        return chapterService.getAll(page);
    }

    @GetMapping("{id}")
    @Transactional
    public ChapterModel getById(@PathVariable Integer id) {
        return chapterService.getById(id);
    }

    @GetMapping
    @Transactional
	public ResponseEntity<Resource> loadChapter(
		@RequestParam Integer chapterId,
		@RequestParam Integer fileId) {
		return ResponseEntity.ok()
			.contentType(MediaType.valueOf("image/jpg"))
			.body(chapterService.loadChapter(chapterId, fileId));
	}

    @PostMapping
    public ChapterModel create(
        @RequestPart("model") String model,
        @RequestPart("files") List<MultipartFile> files) {
            
        ChapterModel chapterModel = loadFromJson(model);
        return chapterService.create(chapterModel, files);
    }

    @PutMapping("{id}")
    public ChapterModel update(
        @PathVariable Integer id,
        @RequestPart("model") String model,
        @RequestPart("files") List<MultipartFile> files) {

        ChapterModel chapterModel = loadFromJson(model);
        return chapterService.update(id, chapterModel, files);
    }

    @DeleteMapping("{id}")
    @Transactional
    public String delete(@PathVariable Integer id) {
        ChapterModel result = chapterService.delete(id);
        return "Xóa thành công chương '" + result.getTitle() + "' của sách " + result.getBookTitle();
    }

    private ChapterModel loadFromJson(String model) {
        ChapterModel chapterModel = null;
        try {
            chapterModel = mapper.readValue(model, ChapterModel.class);
        } catch (Exception e) {
            throw new AppException(HttpStatus.BAD_REQUEST, 
            "Sách sai định dạng");
        }
        return chapterModel;
    }
}
