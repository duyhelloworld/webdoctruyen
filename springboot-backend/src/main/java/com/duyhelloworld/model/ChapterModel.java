package com.duyhelloworld.model;

import java.time.LocalDateTime;

import org.springframework.util.StringUtils;

import com.fasterxml.jackson.annotation.JsonFormat;

import lombok.Data;
import com.duyhelloworld.entity.Chapter;

@Data
public class ChapterModel {
    private Integer chapterId;

    private String title;
    
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime uploadAt;

    private int commentCount;

    private int numberOfImage;

    private Integer bookId;
    private String bookTitle;

    public static ChapterModel convert(Chapter chapter) {
        ChapterModel chapterModel = new ChapterModel();
        chapterModel.setChapterId(chapter.getId());

        if (StringUtils.hasText(chapter.getTitle())) {
            chapterModel.setTitle(chapter.getTitle());
        } else {
            chapterModel.setTitle("Chapter_" + chapter.getId());
        }
        chapterModel.setUploadAt(chapter.getUploadAt());

        chapterModel.setCommentCount(
            chapter.getComments() != null
            ? chapter.getComments().size()
            : 0);
            
        chapterModel.setBookId(chapter.getBook().getId());
        chapterModel.setBookTitle(chapter.getBook().getTitle());
        return chapterModel;
    }

    public static ChapterModel convert(Chapter chapter, Integer numberOfImage) {
        ChapterModel chapterModel = new ChapterModel();
        chapterModel.setChapterId(chapter.getId());
        chapterModel.setTitle(chapter.getTitle());
        chapterModel.setUploadAt(chapter.getUploadAt());
        chapterModel.setNumberOfImage(numberOfImage);

        chapterModel.setCommentCount(chapter.getComments() != null
            ? chapter.getComments().size()
            : 0);
            
        chapterModel.setBookId(chapter.getBook().getId());
        chapterModel.setBookTitle(chapter.getBook().getTitle());
        return chapterModel;
    }

    public static Chapter convert(ChapterModel chapterModel) {
        Chapter chapter = new Chapter();
        chapter.setId(chapterModel.getChapterId());
        chapter.setTitle(chapterModel.getTitle());
        return chapter;
    }
}
