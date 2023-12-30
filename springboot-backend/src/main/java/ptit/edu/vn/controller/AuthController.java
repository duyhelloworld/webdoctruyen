package ptit.edu.vn.controller;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.core.Authentication;
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
import ptit.edu.vn.entity.Role;
import ptit.edu.vn.entity.TokenDied;
import ptit.edu.vn.entity.User;
import ptit.edu.vn.exception.AppException;
import ptit.edu.vn.model.AuthModel;
import ptit.edu.vn.model.ChangePassModel;
import ptit.edu.vn.model.SignInModel;
import ptit.edu.vn.model.SignUpModel;
import ptit.edu.vn.repository.TokenDiedRepository;
import ptit.edu.vn.repository.UserRepository;
import ptit.edu.vn.service.security.AppUserDetails;
import ptit.edu.vn.service.security.JwtService;

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
    private AuthenticationConfiguration authConfiguration;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private ObjectMapper mapper;

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

        // Đăng kí tài khoản luôn là USER
        User user = new User(
            signUpModel.getUsername(),
            signUpModel.getEmail(),
            passwordEncoder.encode(signUpModel.getPassword()),
            Role.USER);
        user.setAvatar("default-avatar.png");
        User response = userRepository.save(user);
        return ResponseEntity.ok(
            new AuthModel(user.getUsername(), user.getEmail(), jwtService.generateToken(AppUserDetails.build(response))));
    }

    @PostMapping("signin")
    public ResponseEntity<AuthModel> signIn(
        @RequestBody SignInModel signInModel) {
        try {
            Authentication authentication = authConfiguration
                .getAuthenticationManager()
                .authenticate(new UsernamePasswordAuthenticationToken(
                    signInModel.getUsername(), signInModel.getPassword()));

            SecurityContextHolder.getContext().setAuthentication(authentication);
            AppUserDetails userDetails = (AppUserDetails) authentication.getPrincipal();
            return ResponseEntity.ok(new AuthModel(userDetails.getUsername(), userDetails.getEmail(), jwtService.generateToken(userDetails)));
        } catch (Exception e) {
            e.printStackTrace();
        }
        throw new AppException(HttpStatus.BAD_REQUEST, "Đăng nhập thất bại");
    }

    @PostMapping("change-password")
    public ResponseEntity<String> changePass(@RequestBody ChangePassModel model,
        HttpServletRequest request) {
        String token = jwtService.getTokenFromRequest(request);
        if (token == null) {
            throw new AppException(HttpStatus.UNAUTHORIZED, "Bạn cần đăng nhập để thực hiện hành động này!");
        }
        Integer uid = jwtService.getUserIdFromToken(token);
        if (uid == null) {
            throw new AppException(HttpStatus.BAD_REQUEST, "Lỗi xác thực. Vui lòng đăng nhập lại");
        }
        if (!StringUtils.hasText(model.getOldPassword()))
            throw new AppException(HttpStatus.NOT_FOUND, "Thiếu password cũ");
        if (!StringUtils.hasLength(model.getNewPassword()))
            throw new AppException(HttpStatus.NOT_FOUND, "Thiếu password mới");
        if(model.getNewPassword().equals(model.getOldPassword()))
            throw new AppException(HttpStatus.BAD_REQUEST, "Mật khẩu mới không được trùng với mật khẩu cũ");
        User user = userRepository.findById(uid)
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
