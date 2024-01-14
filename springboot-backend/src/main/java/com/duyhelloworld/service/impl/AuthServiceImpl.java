package com.duyhelloworld.service.impl;

import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import com.duyhelloworld.entity.Role;
import com.duyhelloworld.entity.TokenDied;
import com.duyhelloworld.entity.User;
import com.duyhelloworld.exception.AppException;
import com.duyhelloworld.model.AuthModel;
import com.duyhelloworld.model.ChangePassModel;
import com.duyhelloworld.model.SignInModel;
import com.duyhelloworld.model.SignUpModel;
import com.duyhelloworld.repository.TokenDiedRepository;
import com.duyhelloworld.repository.UserRepository;
import com.duyhelloworld.service.AuthService;
import com.duyhelloworld.service.FileService;
import com.duyhelloworld.service.security.local.JwtService;
import com.duyhelloworld.service.security.providers.LocalUser;

import jakarta.servlet.http.HttpServletRequest;
import lombok.AllArgsConstructor;

@Service
@AllArgsConstructor
public class AuthServiceImpl implements AuthService {

    private UserRepository userRepository;

    private TokenDiedRepository tokenDiedRepository;

    private JwtService jwtService;

    private AuthenticationManager authenticationManager;

    private PasswordEncoder passwordEncoder;

    private FileService fileService;

    @Override
    public AuthModel signUp(SignUpModel signUpModel, MultipartFile avatar) {
        if (!StringUtils.hasText(signUpModel.getUsername()))
            throw new AppException(HttpStatus.NOT_FOUND, "Thiếu username");
        if (!StringUtils.hasLength(signUpModel.getPassword()))
            throw new AppException(HttpStatus.NOT_FOUND, "Thiếu password");
        if (!StringUtils.hasText(signUpModel.getEmail()))
            throw new AppException(HttpStatus.NOT_FOUND, "Thiếu email");

        if (userRepository.existsByUsername(signUpModel.getUsername())) {
            throw new AppException(HttpStatus.BAD_REQUEST, "Username này đã tồn tại");
        }
        if (userRepository.existsByEmail(signUpModel.getEmail())) {
            throw new AppException(HttpStatus.BAD_REQUEST, "Email này đã tồn tại");
        }
        String fileNameResult = fileService.saveAvatar(avatar);
        // Đăng kí tài khoản luôn là USER
        User user = new User(
            signUpModel.getUsername(),
            signUpModel.getEmail(),
            passwordEncoder.encode(signUpModel.getPassword()),
            signUpModel.getFullname(),
            fileNameResult,
            Role.USER);
        userRepository.save(user);
        return new AuthModel(user.getUsername(),
            user.getEmail(), 
            user.getFullname(),
            user.getAvatar(),
            jwtService.generateToken(new LocalUser(user)));
    }

    @Override
    public AuthModel signIn(SignInModel signInModel) {
        try {
            Authentication authentication = authenticationManager
                .authenticate(new UsernamePasswordAuthenticationToken(
                    signInModel.getUsername(), signInModel.getPassword()));

            SecurityContextHolder.getContext().setAuthentication(authentication);
            LocalUser userDetails = (LocalUser) authentication.getPrincipal();
            return new AuthModel(userDetails.getUsername(),
            userDetails.getEmail(), 
            userDetails.getFullname(),
            userDetails.getAvatar(),
            jwtService.generateToken(new LocalUser(userDetails.getUser())));
        } catch (Exception e) {
            throw new AppException(HttpStatus.BAD_REQUEST, "Sai tên đăng nhập hoặc mật khẩu");
        }
    }

    @Override
    public String changePassword(ChangePassModel changePassModel, User user) {
        if (!StringUtils.hasText(changePassModel.getOldPassword()))
            throw new AppException(HttpStatus.NOT_FOUND, "Thiếu password cũ");
        if (!StringUtils.hasLength(changePassModel.getNewPassword()))
            throw new AppException(HttpStatus.NOT_FOUND, "Thiếu password mới");
        if(changePassModel.getNewPassword().equals(changePassModel.getOldPassword()))
            throw new AppException(HttpStatus.BAD_REQUEST, "Mật khẩu mới không được trùng với mật khẩu cũ");
        if (!passwordEncoder.matches(changePassModel.getOldPassword(), user.getPassword())) {
            throw new AppException(HttpStatus.BAD_REQUEST, "Mật khẩu cũ không đúng");
        }
        user.setPassword(passwordEncoder.encode(changePassModel.getNewPassword()));
        userRepository.save(user);
        return "Đổi mật khẩu thành công";
    }

    @Override
    public String signOut(User user, HttpServletRequest request) {
        String jwt = jwtService.getTokenFromRequest(request);
        if (tokenDiedRepository.existsByToken(jwt)) {
            throw new AppException(HttpStatus.BAD_REQUEST, "Bạn đã đăng xuất rồi");
        }
        SecurityContextHolder.getContext().setAuthentication(null);
        tokenDiedRepository.save(new TokenDied(user, jwt));
        return "Đăng xuất thành công";
    }
    
}
