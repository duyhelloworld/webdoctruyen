package com.duyhelloworld.service;

import java.util.List;

import com.duyhelloworld.entity.User;
import com.duyhelloworld.model.CommentModel;

public interface CommentService {
    public List<CommentModel> getAll(Integer page);
    
    public List<CommentModel> getByChapterId(Integer chapterId);

    public CommentModel getById(Integer id);

    public CommentModel create(CommentModel commentModel, User user);

    public CommentModel update(Integer id, CommentModel commentModel, User user);

    public void delete(Integer id, User user);
}
