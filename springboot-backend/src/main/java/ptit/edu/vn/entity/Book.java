package ptit.edu.vn.entity;

import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Data
@Entity
public class Book {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(unique = true, nullable = false)
    private String title;
    
    private LocalDate releaseDate;

    @Column(nullable = false)
    private LocalDateTime uploadAt;
    
    @Column(nullable = false, length = 200)
    private String coverImage;

    @Column(length = 255)
    private String author;

    @Column(columnDefinition = "text")
    private String description;

    @ManyToMany(mappedBy = "Books", fetch = FetchType.EAGER)
    private List<Category> Categories;
    
    @OneToMany(mappedBy = "book", fetch = FetchType.LAZY)
    private List<Chapter> Chapters;

    @OneToMany(mappedBy = "book", fetch = FetchType.EAGER)
    private List<Rating> Ratings;
}