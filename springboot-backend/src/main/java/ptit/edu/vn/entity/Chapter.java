package ptit.edu.vn.entity;

import java.time.LocalDateTime;
import java.util.List;

import jakarta.persistence.*;
import lombok.Data;

@Data
@Entity
public class Chapter {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(nullable = false)
    private String folderName;

    @Column(nullable = false)
    private String title;
    
    private LocalDateTime uploadAt;

    @ManyToOne
    @JoinColumn(name = "book_id", nullable = false)
    private Book book;

    @OneToMany(mappedBy = "chapter", fetch = FetchType.LAZY)
    private List<Comment> Comments;
}
