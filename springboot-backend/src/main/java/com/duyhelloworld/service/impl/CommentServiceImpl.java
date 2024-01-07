package com.duyhelloworld.service.impl;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import com.duyhelloworld.configuration.AppConstant;
import com.duyhelloworld.entity.Chapter;
import com.duyhelloworld.entity.Comment;
import com.duyhelloworld.entity.User;
import com.duyhelloworld.exception.AppException;
import com.duyhelloworld.model.CommentModel;
import com.duyhelloworld.repository.ChapterRepository;
import com.duyhelloworld.repository.CommentRepository;
import com.duyhelloworld.service.CommentService;

import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;

@Service
@AllArgsConstructor
public class CommentServiceImpl implements CommentService {

    private CommentRepository commentRepository;

    private ChapterRepository chapterRepository;

    @Override
    public List<CommentModel> getAll(Integer page) {
        if (page == null) {
            page = 0;
        }
        List<Comment> comments = commentRepository.findAll(
            PageRequest.of(page, AppConstant.PAGE_SIZE).withPage(page))
            .getContent();
        return comments.stream()
            .map(CommentModel::convert)
            .collect(Collectors.toList());
    }

    @Override
    public List<CommentModel> getByChapterId(Integer chapterId) {
       Chapter chapter = chapterRepository.findById(chapterId)
            .orElseThrow(() -> 
            new AppException(HttpStatus.NOT_FOUND, "Không tìm thấy chương truyện mã " + chapterId));
        return commentRepository.findByChapter(chapter).stream()
            .map(CommentModel::convert)
            .collect(Collectors.toList());
    }

    @Override
    public CommentModel getById(Integer id) {
        Comment comment = commentRepository.findById(id)
            .orElseThrow(() -> new AppException(HttpStatus.NOT_FOUND, "Không tìm thấy bình luận mã " + id));
        return CommentModel.convert(comment);
    }

    @Override
    @Transactional
    public CommentModel create(CommentModel commentModel, User user) {
        if (commentModel.getChapterId() == null) {
            throw new AppException(HttpStatus.BAD_REQUEST, "Chương truyện không được để trống");
        }
        Comment comment = new Comment();
        Chapter chapter = chapterRepository.findById(commentModel.getChapterId())
            .orElseThrow(() -> new AppException(HttpStatus.NOT_FOUND, "Không tìm thấy chương truyện mã " + commentModel.getChapterId()));
        comment.setChapter(chapter);
        comment.setContent(commentModel.getContent());
        comment.setCommentAt(LocalDateTime.now());
        comment.setIsEdited(false);
        comment.setUser(user);
        commentRepository.save(comment);
        return CommentModel.convert(comment);
    }

    @Override
    @Transactional
    public CommentModel update(Integer id, CommentModel commentModel, User user) {
        Comment comment = commentRepository.findById(commentModel.getId()).orElse(null);
        if (comment == null)
            throw new AppException(HttpStatus.NOT_FOUND, "Không tìm thấy bình luận");
        if (comment.getUser().getId() != user.getId())
            throw new AppException(HttpStatus.FORBIDDEN, "Bạn không có quyền chỉnh sửa bình luận này");
        comment.setContent(commentModel.getContent());
        comment.setCommentAt(LocalDateTime.now());
        comment.setIsEdited(true);
        commentRepository.save(comment);
        return CommentModel.convert(comment);
    }

    @Override
    @Transactional
    public void delete(Integer id, User user) {
        Comment comment = commentRepository.findById(id).orElse(null);
        if (comment == null) {
            throw new AppException(HttpStatus.NOT_FOUND, "Không tìm thấy bình luận");
        }
        if (comment.getUser().getId() != user.getId()) {
            throw new AppException(HttpStatus.FORBIDDEN, "Bạn không có quyền xóa bình luận này");
        }
        commentRepository.delete(comment);
        System.out.println("Xóa thành công bình luận mã " + id);
    }
    
}
