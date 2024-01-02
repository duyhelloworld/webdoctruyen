package com.duyhelloworld.entity;

import jakarta.persistence.*;
import lombok.Data;

import java.util.List;

@Data
@Entity
public class Category {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(unique = true, nullable = false)
    private String name;

    @Column(columnDefinition = "text")
    private String description;

    @ManyToMany
    @JoinTable(name = "Book_Category",
            joinColumns = @JoinColumn(name = "CategoryId"),
            inverseJoinColumns = @JoinColumn(name = "BookId"))
    private List<Book> Books;
}
