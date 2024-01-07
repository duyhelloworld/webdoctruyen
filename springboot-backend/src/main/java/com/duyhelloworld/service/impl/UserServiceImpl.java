package com.duyhelloworld.service.impl;

import java.util.Collection;

import org.springframework.core.io.Resource;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import com.duyhelloworld.entity.User;
import com.duyhelloworld.exception.AppException;
import com.duyhelloworld.model.UserModel;
import com.duyhelloworld.repository.UserRepository;
import com.duyhelloworld.service.FileService;
import com.duyhelloworld.service.UserService;

@Service
public class UserServiceImpl implements UserService {
    private UserRepository userRepository;

    private FileService fileService;

    @Override
    public Collection<UserModel> findAll() {
        return userRepository.findAll().stream()
            .map(UserModel::convert).toList();
    }

    @Override
    public UserModel findById(Integer id) {
        return userRepository.findById(id)
            .map(UserModel::convert)
            .orElseThrow(() -> new AppException(HttpStatus.NOT_FOUND,
            "Không tìm thấy user mã " + id));
    }

    @Override
    public Resource loadAvatar(User user) {
        return fileService.getAvatar(user.getAvatar());
    }
}
