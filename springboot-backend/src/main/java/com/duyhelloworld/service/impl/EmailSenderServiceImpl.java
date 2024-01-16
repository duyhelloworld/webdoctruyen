package com.duyhelloworld.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.duyhelloworld.service.EmailSenderService;

import jakarta.mail.internet.MimeMessage;

@Service
public class EmailSenderServiceImpl implements EmailSenderService {

    @Autowired
    private JavaMailSender mailSender;

    @Override
    public void sendEmail(String to, String subject, String content) {
        try {
            System.out.println("Đang gửi mail tới " + to);
            SimpleMailMessage mailMessage = new SimpleMailMessage();
            mailMessage.setFrom("duy0184466@huce.edu.vn");
            mailMessage.setTo(to);
            mailMessage.setSubject(subject);
            mailMessage.setText(content);
            mailSender.send(mailMessage);
            System.out.println("Đã gửi mail tới " + to);
        } catch (Exception e) {
            System.out.println("Lỗi gửi mail tới " + to);
            e.printStackTrace();
        }
    }

    @Override
    public void sendEmailWithAttachment(String to, String subject, String content, MultipartFile file) {
        try {
            MimeMessage mailMessage = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(mailMessage, true, "utf-8");
            helper.setSubject(subject);
            helper.setTo(to);
            helper.setText(content);
            helper.setFrom("duy0184466@huce.edu.vn");
            helper.addAttachment(file.getOriginalFilename(), file);
            mailSender.send(mailMessage);
        } catch (Exception e) {
            System.out.println("Lỗi gửi mail tới " + to);
            e.printStackTrace();
        }
    }
}
