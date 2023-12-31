package ptit.edu.vn.service;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import ptit.edu.vn.entity.Role;
import ptit.edu.vn.entity.User;
import ptit.edu.vn.repository.UserRepository;

@Component
public class FakeUserService 
// implements CommandLineRunner 
{
    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private UserRepository userRepository;

    // @Override
    public void run(String... args) throws Exception {
        String pass = passwordEncoder.encode("12345678");
        List<User> users = new ArrayList<>();
        users.add(new User("ngovannam123", "ngovannam123@gmail.com", pass, "default-avatar.png", "ngô văn nam", Role.USER));
        users.add(new User("levanbang123", "levanbang123@gmail.com" , pass, "default-avatar.png", "lê văn bằng", Role.USER));
        users.add(new User("nguyenducluong123", "nguyenducluong@gmail.com", pass, "default-avatar.png", "nguyễn đức lương", Role.USER));
        users.add(new User("tranhuudat123", "tranhuudat@gmail.com", pass, "default-avatar.png", "trần hữu đạt", Role.USER));
        users.add(new User("admin", "admin@gmail.com", pass, "default-avatar.png", "Adminitrastor", Role.ADMIN));
        if (userRepository.count() > 0) {
            return;
        }
        userRepository.saveAll(users);
    }
}
