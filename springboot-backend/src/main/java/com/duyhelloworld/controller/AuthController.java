package com.duyhelloworld.controller;


import org.springframework.http.HttpStatus;
import org.springframework.security.core.annotation.AuthenticationPrincipal;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.fasterxml.jackson.databind.ObjectMapper;

import jakarta.servlet.http.HttpServletRequest;
import lombok.AllArgsConstructor;

import com.duyhelloworld.exception.AppException;
import com.duyhelloworld.model.AuthModel;
import com.duyhelloworld.model.ChangePassModel;
import com.duyhelloworld.model.SignInModel;
import com.duyhelloworld.model.SignUpModel;
import com.duyhelloworld.service.AuthService;
import com.duyhelloworld.service.security.AppUserDetail;

@RestController
@RequestMapping("api/auth")
@AllArgsConstructor
public class AuthController {

    private ObjectMapper mapper;

    private AuthService authService;

    @PostMapping("signup")
    public AuthModel signUp(
        @RequestPart String jsonModel,
        @RequestPart(required = false) MultipartFile avatar) {
        SignUpModel signUpModel = null;
        try {
            signUpModel = mapper.readValue(jsonModel, SignUpModel.class);
        } catch (Exception e) {
            throw new AppException(HttpStatus.BAD_REQUEST, "Dữ liệu không hợp lệ");
        }
        return authService.signUp(signUpModel, avatar);
    }

    @PostMapping("signin")
    public AuthModel signIn(
        @RequestBody SignInModel signInModel) {
        return authService.signIn(signInModel);
    }

    @PostMapping("change-password")
    public String changePass(@RequestBody ChangePassModel model, 
    @AuthenticationPrincipal AppUserDetail userInfo) {
        return authService.changePassword(model, userInfo.getUser());
    }

    @PostMapping("signout")
    public String signOut(
        @AuthenticationPrincipal AppUserDetail userInfo,
        HttpServletRequest request) {
        return authService.signOut(userInfo.getUser(), request);
    }
}
