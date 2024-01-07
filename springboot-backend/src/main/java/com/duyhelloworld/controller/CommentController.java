package com.duyhelloworld.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import jakarta.transaction.Transactional;

import com.duyhelloworld.model.CommentModel;
import com.duyhelloworld.service.AppUserDetail;
import com.duyhelloworld.service.CommentService;

import java.util.List;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
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

    private CommentService commentService;

    @GetMapping("all")
    public List<CommentModel> getAll(@RequestParam(required = false) Integer page) {
        return commentService.getAll(page);
    }

    @GetMapping("{id}")
    public CommentModel getById(@PathVariable Integer id) {
        return commentService.getById(id);
    }

    @GetMapping("chapter/{chapterId}")
    public List<CommentModel> getByChapterId(
        @PathVariable Integer chapterId) {
        return commentService.getByChapterId(chapterId);
    }
    
    @PostMapping
    public CommentModel create(
        @RequestBody CommentModel commentModel,
        @AuthenticationPrincipal AppUserDetail userInfo) {
        return commentService.create(commentModel, userInfo.getUser());
    }

    @Transactional
    @PutMapping("{id}")
    public CommentModel update(
        @RequestBody CommentModel commentModel,
        @PathVariable Integer id,
        @AuthenticationPrincipal AppUserDetail userInfo) {
        return commentService.update(id, commentModel, userInfo.getUser());
    }

    @DeleteMapping("{id}")
    public void delete(@PathVariable Integer id, @AuthenticationPrincipal AppUserDetail userInfo) {
        commentService.delete(id, userInfo.getUser());
    }
}
