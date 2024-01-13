package com.duyhelloworld.controller;

import java.util.Collection;
import org.springframework.core.io.Resource;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.duyhelloworld.configuration.AppConstant;
import com.duyhelloworld.model.UserModel;
import com.duyhelloworld.service.AppUserDetail;
import com.duyhelloworld.service.UserService;

@RestController
@RequestMapping("/api/user")
@CrossOrigin(origins = "*", allowedHeaders = "*")
public class UserController {

    private UserService userService;

    @GetMapping
    public ResponseEntity<Resource> getAvatar(@AuthenticationPrincipal AppUserDetail userInfo) {            
        return ResponseEntity.ok()
        .contentType(AppConstant.USER_AVATAR_FILE_TYPE)
        .body(userService.loadAvatar(userInfo.getUser()));
    }

    @GetMapping("all")
    @PreAuthorize("hasAuthorities('ADMIN')")
    public Collection<UserModel> getAll()  {
        return userService.findAll();
    }
}
