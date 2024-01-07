package com.duyhelloworld.service;

import java.util.List;

import org.springframework.core.io.Resource;
import org.springframework.web.multipart.MultipartFile;

public interface FileService {

    public void createNewBook(String folderName);

    public void saveCoverImage(MultipartFile file);

    public void saveChapter(List<MultipartFile> files, String bookName, String chapterName);
    
    // trả về fileName
    public String saveAvatar(MultipartFile file);

    public Resource getCoverImage(String filename);

    public Resource getAvatar(String filename);

    public Resource getChapter(String bookName, String chapterName, Integer fileId);

    public Integer getNumberImageOfChapter(String bookName, String chapterName);

    public void deleteAvatar(String filename);

    public void deleteCoverImage(String filename);

    public void deleteChapter(String bookName, String chapterName);

    public void deleteBook(String bookName);

}
