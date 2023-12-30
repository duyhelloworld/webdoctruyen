package ptit.edu.vn.service.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import jakarta.transaction.Transactional;
import ptit.edu.vn.entity.User;
import ptit.edu.vn.exception.AppException;
import ptit.edu.vn.repository.UserRepository;

@Service
public class AppUserDetailsService implements UserDetailsService {
    @Autowired
    private UserRepository userRepository;
    
    @Override
    @Transactional
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = userRepository.findByUsername(username)
            .orElseThrow(() -> new UsernameNotFoundException(
                "Không tồn tại tài khoản : " + username));
        return AppUserDetails.build(user);
    }

    public User loadUser(Integer uid) {
        return userRepository.findById(uid)
            .orElseThrow(() -> new AppException(HttpStatus.NOT_FOUND, 
            "Không tồn tại tài khoản này"));
    }
}
