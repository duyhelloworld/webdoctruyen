package ptit.edu.vn.service.security;

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
import ptit.edu.vn.entity.Role;
import ptit.edu.vn.entity.User;
import ptit.edu.vn.exception.AppException;
import ptit.edu.vn.repository.UserRepository;
import ptit.edu.vn.service.security.local.LocalUserDetails;
import ptit.edu.vn.service.security.oauth2.OAuth2UserDetail;
import ptit.edu.vn.service.security.oauth2.OAuth2UserDetailFactory;

@Service
public class UserDetailService extends DefaultOAuth2UserService implements UserDetailsService {
    @Autowired
    private UserRepository userRepository;

    @Transactional
    public User getUser(String username) {
        return userRepository.findByUsername(username)
            .orElseThrow(() -> new UsernameNotFoundException(
                "Không tồn tại tài khoản : " + username));
    }
    
    @Override
    @Transactional
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = getUser(username);
        return LocalUserDetails.build(user);
    }

    @Override
    public OAuth2User loadUser(OAuth2UserRequest userRequest) throws OAuth2AuthenticationException {
        try {
            return processOAuth2User(userRequest, super.loadUser(userRequest));
        } catch (Exception e) {
            throw new AppException(HttpStatus.INTERNAL_SERVER_ERROR, "Lỗi xác thực người dùng.");
        }
    }

    private OAuth2User processOAuth2User(OAuth2UserRequest userRequest, OAuth2User oAuth2User) {
        OAuth2UserDetail oAuthUserDetail = OAuth2UserDetailFactory.getOAuth2UserDetail(
            oAuth2User,
            userRequest.getClientRegistration().getRegistrationId());

        // Check Username is valid
        Optional<User> user = userRepository.findByUsername(oAuthUserDetail.getUsername());
        // Save Db
        User tempUser;
        if (user.isEmpty() || !user.get().getProvider().equals(oAuthUserDetail.getProvider())) {
            tempUser = registerNewUser(oAuthUserDetail);
        } else {
            tempUser = updateExistingUser(user.get(), oAuthUserDetail);
        }
        oAuthUserDetail.setUserId(tempUser.getId());
        return oAuthUserDetail;
    }

    private User registerNewUser(OAuth2UserDetail oAuthUserDetail) {
        User user = new User();
        user.setUsername(oAuthUserDetail.getUsername());
        user.setProvider(oAuthUserDetail.getProvider());
        user.setFullname(oAuthUserDetail.getFullname());
        user.setEmail(oAuthUserDetail.getEmail());
        user.setAvatar(oAuthUserDetail.getAvatar());
        user.setPassword(null);

        user.setEnable(true);
        user.setCredentialsNonExpired(true);
        user.setAccountNonLocked(true);
        user.setAccountNonExpired(true);

        user.setRole(Role.USER);
        return userRepository.save(user);
    }

    private User updateExistingUser(User existingUser, OAuth2UserDetail oAuthUserDetail) {
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
