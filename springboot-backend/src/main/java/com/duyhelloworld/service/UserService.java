package com.duyhelloworld.service;

import java.util.Collection;

import org.springframework.core.io.Resource;

import com.duyhelloworld.entity.User;
import com.duyhelloworld.model.UserModel;

public interface UserService {
    public Collection<UserModel> findAll();

    public UserModel findById(Integer id);

    public Resource loadAvatar(User user);
}
