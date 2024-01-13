package com.duyhelloworld.controller;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/")
@CrossOrigin(origins = "*", allowedHeaders = "*")
public class HomeController {
    @GetMapping
    public String home(@AuthenticationPrincipal Object userInfo) {
        return "Hello " + userInfo;
    }

    @GetMapping("favicon.ico")
    @ResponseBody
    void returnNoFavicon() {
    }

    @GetMapping("/admin")
    @PreAuthorize("hasAuthority('ADMIN') or hasAuthority('SUPER_ADMIN')")
    public String admin(@AuthenticationPrincipal Object userInfo) {
        return "Info : </br>Class: " + userInfo.getClass() + "</br>ToString: " + userInfo;
    }

    @GetMapping("/user")
    @PreAuthorize("hasAuthority('USER')")
    public String user(@AuthenticationPrincipal Object userInfo) {
        return "Info : </br>Class: " + userInfo.getClass() + "</br>ToString: " + userInfo;
    }
}
