package com.duyhelloworld.model;

import java.time.LocalDateTime;

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

        chapterModel.setTitle(chapter.getTitle());
        chapterModel.setUploadAt(chapter.getUploadAt());

        chapterModel.setCommentCount(
            chapter.getComments() != null
            ? chapter.getComments().size()
            : 0);
        if (chapter.getBook() != null) {
            chapterModel.setBookId(chapter.getBook().getId());
            chapterModel.setBookTitle(chapter.getBook().getTitle());
        }
        return chapterModel;
    }

    public static ChapterModel convert(Chapter chapter, Integer numberOfImage) {
        ChapterModel chapterModel = convert(chapter);
        chapterModel.setNumberOfImage(numberOfImage);
        return chapterModel;
    }

    public static Chapter convert(ChapterModel chapterModel) {
        Chapter chapter = new Chapter();
        chapter.setId(chapterModel.getChapterId());
        chapter.setTitle(chapterModel.getTitle());
        return chapter;
    }
}
