package com.duyhelloworld.service;

import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import lombok.AllArgsConstructor;

@Component
@AllArgsConstructor
public class FakeUserService implements CommandLineRunner 
{
    // private PasswordEncoder passwordEncoder;

    // private UserRepository userRepository;  

    private EmailSenderService emailSenderService;    

    // @Override
    public void run(String... args) throws Exception {
        // String pass = passwordEncoder.encode("12345678");
        // List<User> users = new ArrayList<User>();
        // users.add(new User("ngovannam123", "ngovannam123@gmail.com", pass, "default-avatar.png", "ngô văn nam", Role.USER));
        // users.add(new User("levanbang123", "levanbang123@gmail.com" , pass, "default-avatar.png", "lê văn bằng", Role.USER));
        // users.add(new User("nguyenducluong123", "nguyenducluong@gmail.com", pass, "default-avatar.png", "nguyễn đức lương", Role.USER));
        // users.add(new User("tranhuudat123", "tranhuudat@gmail.com", pass, "default-avatar.png", "trần hữu đạt", Role.USER));
        // users.add(new User("admin", "admin@gmail.com", pass, "default-avatar.png", "Adminitrastor", Role.ADMIN));
        // if (userRepository.count() > users.size()) {
        //     return;
        // }
        // userRepository.saveAll(users);
        emailSenderService.sendEmail("duy0184466@huce.edu.vn", 
            "test message",
             "Hello World!");
    }
}
