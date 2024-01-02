package com.duyhelloworld.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import jakarta.transaction.Transactional;
import com.duyhelloworld.configuration.AppConstant;
import com.duyhelloworld.entity.Chapter;
import com.duyhelloworld.entity.Comment;
import com.duyhelloworld.entity.User;
import com.duyhelloworld.exception.AppException;
import com.duyhelloworld.model.CommentModel;
import com.duyhelloworld.repository.ChapterRepository;
import com.duyhelloworld.repository.CommentRepository;
import com.duyhelloworld.repository.UserRepository;
import com.duyhelloworld.service.security.AbstractUserInfo;
import com.duyhelloworld.service.security.local.LocalUserDetails;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

@RestController
@RequestMapping("api/comment")
public class CommentController {

    @Autowired
    private CommentRepository commentRepository;

    @Autowired
    private ChapterRepository chapterRepository;

    @Autowired
    private UserRepository userRepository;

    @GetMapping("all")
    public List<CommentModel> getAll(@RequestParam(required = false) Integer page) {
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

    @GetMapping("{id}")
    public CommentModel getById(@PathVariable Integer id) {
        Comment comment = commentRepository.findById(id)
            .orElseThrow(() -> new AppException(HttpStatus.NOT_FOUND, "Không tìm thấy bình luận mã " + id));
        return CommentModel.convert(comment);
    }

    @GetMapping("chapter/{chapterId}")
    public List<CommentModel> getByChapterId(
        @PathVariable Integer chapterId) {
        Chapter chapter = chapterRepository.findById(chapterId)
            .orElseThrow(() -> 
            new AppException(HttpStatus.NOT_FOUND, "Không tìm thấy chương truyện mã " + chapterId));
        return commentRepository.findByChapter(chapter).stream()
            .map(CommentModel::convert)
            .collect(Collectors.toList());
    }
    
    @PostMapping
    @Transactional
    public ResponseEntity<CommentModel> create(
        @RequestBody CommentModel commentModel,
        @AuthenticationPrincipal AbstractUserInfo userInfo) {
        if (commentModel.getChapterId() == null) {
            throw new AppException(HttpStatus.BAD_REQUEST, "Chương truyện không được để trống");
        }
        Comment comment = new Comment();
        Chapter chapter = chapterRepository.findById(commentModel.getChapterId())
            .orElseThrow(() -> new AppException(HttpStatus.NOT_FOUND, "Không tìm thấy chương truyện mã " + commentModel.getChapterId()));
        User user = userRepository.findById(userInfo.getUserId()).get();
        comment.setChapter(chapter);
        comment.setContent(commentModel.getContent());
        comment.setCommentAt(LocalDateTime.now());
        comment.setIsEdited(false);
        comment.setUser(user);
        commentRepository.save(comment);
        return ResponseEntity.ok(CommentModel.convert(comment));
    }

    @Transactional
    @PutMapping("{id}")
    public ResponseEntity<CommentModel> update(
        @RequestBody CommentModel commentModel,
        @PathVariable Integer id,
        @AuthenticationPrincipal AbstractUserInfo userInfo) {
        Comment comment = commentRepository.findById(commentModel.getId()).orElse(null);
        if (comment == null)
            throw new AppException(HttpStatus.NOT_FOUND, "Không tìm thấy bình luận");
        if (comment.getUser().getId() != userInfo.getUserId())
            throw new AppException(HttpStatus.FORBIDDEN, "Bạn không có quyền chỉnh sửa bình luận này");
        comment.setContent(commentModel.getContent());
        comment.setCommentAt(LocalDateTime.now());
        comment.setIsEdited(true);
        commentRepository.save(comment);
        return ResponseEntity.ok(CommentModel.convert(comment));
    }

    @DeleteMapping("{id}")
    public ResponseEntity<CommentModel> delete(@RequestParam Integer id) {
        LocalUserDetails appUserDetails = (LocalUserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        Comment comment = commentRepository.findById(id).orElse(null);
        if (comment == null) {
            throw new AppException(HttpStatus.NOT_FOUND, "Không tìm thấy bình luận");
        }
        if (comment.getUser().getId() != appUserDetails.getUserId()) {
            throw new AppException(HttpStatus.FORBIDDEN, "Bạn không có quyền xóa bình luận này");
        }
        commentRepository.delete(comment);
        return ResponseEntity.ok(CommentModel.convert(comment));
    }
}
