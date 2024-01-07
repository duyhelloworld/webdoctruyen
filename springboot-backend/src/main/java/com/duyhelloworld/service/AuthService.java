package com.duyhelloworld.service;

import org.springframework.web.multipart.MultipartFile;

import com.duyhelloworld.entity.User;
import com.duyhelloworld.model.AuthModel;
import com.duyhelloworld.model.ChangePassModel;
import com.duyhelloworld.model.SignInModel;
import com.duyhelloworld.model.SignUpModel;

import jakarta.servlet.http.HttpServletRequest;

public interface AuthService {
    public AuthModel signUp(SignUpModel signUpModel, MultipartFile avatar);

    public AuthModel signIn(SignInModel signInModel);

    public String changePassword(ChangePassModel changePassModel, User user);

    public String signOut(User user, HttpServletRequest request);
}
