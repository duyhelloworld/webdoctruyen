package com.duyhelloworld.model;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

import lombok.Data;
import com.duyhelloworld.entity.Book;

@Data
// @JsonPropertyOrder(value = {"id", "title", "releaseDate", "uploadAt", "coverImage", "author", "ratingAverage", "chapters", "categories", "description"})
public class BookModel {
    private Integer id;

    private String title;
    
    @JsonFormat(pattern = "yyyy-MM-dd")
    private LocalDate releaseDate;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime uploadAt;
    
    private String coverImage;

    private String author;

    private Float ratingAverage;
    
    private List<Integer> Chapters;

    private List<String> Categories;

    private String description;
    
    public static Book convert(BookModel bookResponse) {
        Book book = new Book();
        book.setId(bookResponse.getId());
        book.setTitle(bookResponse.getTitle().replace("_", " "));
        book.setReleaseDate(bookResponse.getReleaseDate());
        book.setUploadAt(bookResponse.getUploadAt());
        book.setCoverImage(bookResponse.getCoverImage());
        book.setAuthor(bookResponse.getAuthor());
        book.setDescription(bookResponse.getDescription());
        return book;
    }

    public static BookModel convert(Book book) {
        BookModel bookResponse = new BookModel();
        bookResponse.setId(book.getId());
        bookResponse.setTitle(book.getTitle().replace("_", " "));
        bookResponse.setReleaseDate(book.getReleaseDate());
        bookResponse.setUploadAt(book.getUploadAt());
        bookResponse.setCoverImage(book.getCoverImage());
        bookResponse.setAuthor(book.getAuthor());
        bookResponse.setDescription(book.getDescription());

        if (book.getChapters() != null) {
            bookResponse.setChapters(book.getChapters().stream()
                .map(chapter -> chapter.getId())
                .toList());
        }
        if (book.getCategories() != null && !book.getRatings().isEmpty()) {
            bookResponse.setCategories(book.getCategories().stream()
                .map(category -> category.getName())
                .toList());
        }
        
        if (book.getRatings() != null) {
            int ratingSum = book.getRatings()
                .stream()
                .mapToInt(rating -> rating.getRateStar())
                .sum();
            bookResponse.setRatingAverage(ratingSum / 5 * 1.0f);
        }
        return bookResponse;
    }
}
