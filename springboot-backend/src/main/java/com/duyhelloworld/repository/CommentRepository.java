package com.duyhelloworld.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.duyhelloworld.entity.Chapter;
import com.duyhelloworld.entity.Comment;
import java.util.List;


@Repository
public interface CommentRepository extends JpaRepository<Comment, Integer> {
    List<Comment> findByChapter(Chapter chapter);
}
