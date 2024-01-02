package com.duyhelloworld.service;

import java.util.Collection;

import org.springframework.stereotype.Service;

import com.duyhelloworld.model.UserModel;
import com.duyhelloworld.service.security.AbstractUserInfo;

@Service
public interface UserService {
    public Collection<UserModel> findAll();

    public UserModel findById(Long id);

    public UserModel loadByLogin(AbstractUserInfo userInfo);

    public boolean update(UserModel userModel);
}
