package com.duyhelloworld.service.security;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import jakarta.transaction.Transactional;
import com.duyhelloworld.entity.Role;
import com.duyhelloworld.entity.User;
import com.duyhelloworld.exception.AppException;
import com.duyhelloworld.repository.UserRepository;
import com.duyhelloworld.service.AppUserDetail;
import com.duyhelloworld.service.security.usertype.FacebookUser;
import com.duyhelloworld.service.security.usertype.GithubUser;
import com.duyhelloworld.service.security.usertype.GoogleUser;
import com.duyhelloworld.service.security.usertype.LocalUser;

@Service
public class UserDetailService extends DefaultOAuth2UserService implements UserDetailsService {
    @Autowired
    private UserRepository userRepository;

    private User getUser(String username) {
        return userRepository.findByUsername(username)
            .orElseThrow(() -> new UsernameNotFoundException(
                "Không tồn tại tài khoản : " + username));
    }
    
    @Override
    @Transactional
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = getUser(username);
        return new LocalUser(user);
    }

    @Override
    public OAuth2User loadUser(OAuth2UserRequest userRequest) throws OAuth2AuthenticationException {
        try {
            return processOAuth2User(userRequest, super.loadUser(userRequest));
        } catch (Exception e) {
            e.printStackTrace();
            throw new AppException(HttpStatus.INTERNAL_SERVER_ERROR, "Lỗi xác thực người dùng.");
        }
    }

    private OAuth2User processOAuth2User(OAuth2UserRequest userRequest, OAuth2User oAuth2User) {
        Provider provider = Provider.valueOf(userRequest.getClientRegistration().getRegistrationId().toUpperCase());
        AppUserDetail userDetail;
        switch (provider) {
            case FACEBOOK:
                userDetail = new FacebookUser(oAuth2User);
                break;
            case GOOGLE:
                userDetail = new GoogleUser(oAuth2User);
                break;
            case GITHUB:
                userDetail = new GithubUser(oAuth2User);
                break;
            default:
                throw new AppException(HttpStatus.BAD_REQUEST, "Không hỗ trợ đăng nhập bằng " + provider);
        }
        Optional<User> inDb = userRepository.findByUsername(userDetail.getUsername());
        // Save Db
        User tempUser;
        if (inDb.isEmpty() || !inDb.get().getProvider().equals(userDetail.getProvider())) {
            tempUser = registerNewUser(userDetail);
            System.out.println("Đăng kí thành công tài khoản " + tempUser.getEmail());
        } else {
            tempUser = updateExistingUser(inDb.get(), userDetail);
            System.out.println("Đăng nhập thành công tài khoản " + tempUser.getEmail());
        }
        userDetail.setUser(tempUser);
        return userDetail;
    }

    private User registerNewUser(AppUserDetail oAuthUserDetail) {
        User user = new User();
        if (oAuthUserDetail.getUsername() == null) {
            throw new AppException(HttpStatus.BAD_REQUEST, "Không thể đăng ký tài khoản.");
        }
        user.setUsername(oAuthUserDetail.getUsername());
        user.setProvider(oAuthUserDetail.getProvider());
        user.setFullname(oAuthUserDetail.getFullname());
        user.setEmail(oAuthUserDetail.getEmail());
        user.setAvatar(oAuthUserDetail.getAvatar());
        user.setPassword(null);
        user.setEnabled(true);
        user.setCredentialsNonExpired(true);
        user.setAccountNonLocked(true);
        user.setAccountNonExpired(true);

        user.setRole(Role.USER);
        return userRepository.save(user);
    }

    private User updateExistingUser(User existingUser, AppUserDetail oAuthUserDetail) {
        if (!StringUtils.hasText(existingUser.getEmail())) {
            existingUser.setEmail(oAuthUserDetail.getEmail());
        }
        if (!StringUtils.hasText(existingUser.getFullname())) {
            existingUser.setFullname(oAuthUserDetail.getFullname());
        }
        if (!StringUtils.hasText(existingUser.getAvatar())) {
            existingUser.setAvatar(oAuthUserDetail.getAvatar());
        }
        if (existingUser.getProvider() == null) {
            existingUser.setProvider(oAuthUserDetail.getProvider());
        }
        return userRepository.save(existingUser);
    }
}
