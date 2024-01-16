package com.duyhelloworld.service;

import org.springframework.web.multipart.MultipartFile;

public interface EmailSenderService {

    public void sendEmail(String to, String subject, String content);
    
    public void sendEmailWithAttachment(String to, String subject, String content, MultipartFile file);
}