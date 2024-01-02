package com.duyhelloworld.controller;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.fasterxml.jackson.databind.ObjectMapper;

import jakarta.servlet.http.HttpServletRequest;
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
import com.duyhelloworld.service.file.FileService;
import com.duyhelloworld.service.security.AbstractUserInfo;
import com.duyhelloworld.service.security.local.JwtService;
import com.duyhelloworld.service.security.local.LocalUserDetails;

@RestController
@RequestMapping("api/auth")
public class AuthController {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private TokenDiedRepository tokenDiedRepository;
    
    @Autowired
    private JwtService jwtService;

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private ObjectMapper mapper;

    @Autowired
    private FileService fileService;

     @PostMapping("signup")
    public ResponseEntity<AuthModel> signUp(
        @RequestPart String jsonModel,
        @RequestPart(required = false) MultipartFile avatar) {
        SignUpModel signUpModel = null;
        try {
            signUpModel = mapper.readValue(jsonModel, SignUpModel.class);
        } catch (Exception e) {
            throw new AppException(HttpStatus.BAD_REQUEST, "Dữ liệu không hợp lệ");
        }
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
        User response = userRepository.save(user);
        return ResponseEntity.ok(
            new AuthModel(user.getUsername(), user.getEmail(), jwtService.generateToken(LocalUserDetails.build(response))));
    }

    @PostMapping("signin")
    public ResponseEntity<AuthModel> signIn(
        @RequestBody SignInModel signInModel) {
        try {
            Authentication authentication = authenticationManager
                .authenticate(new UsernamePasswordAuthenticationToken(
                    signInModel.getUsername(), signInModel.getPassword()));

            SecurityContextHolder.getContext().setAuthentication(authentication);
            LocalUserDetails userDetails = (LocalUserDetails) authentication.getPrincipal();
            return ResponseEntity.ok(new AuthModel(userDetails.getUsername(), userDetails.getEmail(), jwtService.generateToken(userDetails)));
        } catch (Exception e) {
            e.printStackTrace();
        }
        throw new AppException(HttpStatus.BAD_REQUEST, "Đăng nhập thất bại");
    }

    @PostMapping("change-password")
    public ResponseEntity<String> changePass(@RequestBody ChangePassModel model, 
    @AuthenticationPrincipal AbstractUserInfo userInfo) {
        if (!StringUtils.hasText(model.getOldPassword()))
            throw new AppException(HttpStatus.NOT_FOUND, "Thiếu password cũ");
        if (!StringUtils.hasLength(model.getNewPassword()))
            throw new AppException(HttpStatus.NOT_FOUND, "Thiếu password mới");
        if(model.getNewPassword().equals(model.getOldPassword()))
            throw new AppException(HttpStatus.BAD_REQUEST, "Mật khẩu mới không được trùng với mật khẩu cũ");
        User user = userRepository.findById(userInfo.getUserId())
                    .orElseThrow(() -> new AppException(HttpStatus.NOT_FOUND, "Tài khoản có thể đã bị xoá bởi quản trị viên"));
        if (!passwordEncoder.matches(model.getOldPassword(), user.getPassword())) {
            throw new AppException(HttpStatus.BAD_REQUEST, "Mật khẩu cũ không đúng");
        }
        user.setPassword(passwordEncoder.encode(model.getNewPassword()));
        userRepository.save(user);
        return ResponseEntity.ok("Đổi mật khẩu thành công");
    }

    @PostMapping("signout")
    public ResponseEntity<String> signOut(HttpServletRequest request) {
        String jwt = jwtService.getTokenFromRequest(request);
        if (!StringUtils.hasLength(jwt)) {
            throw new AppException(HttpStatus.UNAUTHORIZED, "Xác thực không hợp lệ");
        }
        String username = jwtService.getUsernameFromToken(jwt);
        if (!StringUtils.hasLength(username)) {
            throw new AppException(HttpStatus.BAD_REQUEST, "Lỗi xác thực. Vui lòng đăng nhập lại");
        }
        User user = userRepository.findByUsername(username)
                    .orElseThrow(() -> new AppException(HttpStatus.NOT_FOUND, "Tài khoản có thể đã bị xoá bởi quản trị viên"));
        if (tokenDiedRepository.existsByToken(jwt)) {
            throw new AppException(HttpStatus.BAD_REQUEST, "Bạn đã đăng xuất rồi");
        }
        SecurityContextHolder.getContext().setAuthentication(null);
        tokenDiedRepository.save(new TokenDied(user, jwt));
        return ResponseEntity.ok("Đăng xuất thành công");
    }
}
