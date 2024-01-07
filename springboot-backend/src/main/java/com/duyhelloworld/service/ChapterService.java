package com.duyhelloworld.service;

import java.util.List;

import org.springframework.core.io.Resource;
import org.springframework.web.multipart.MultipartFile;

import com.duyhelloworld.model.ChapterModel;

public interface ChapterService {
    public List<ChapterModel> getAll(Integer page);

    public ChapterModel getById(Integer id);

    public Resource loadChapter(Integer chapterId, Integer fileId);

    public ChapterModel create(ChapterModel chapterModel, List<MultipartFile> files);

    public ChapterModel update(Integer id, ChapterModel chapterModel, List<MultipartFile> files);

    public ChapterModel delete(Integer id);
}
