package ptit.edu.vn.controller;

import java.time.LocalDateTime;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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
import ptit.edu.vn.security.AppUserDetails;
import ptit.edu.vn.security.JwtService;

@CrossOrigin(origins = "http://localhost:3000")
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

    // private FileService fileService;    
    // private ObjectMapper mapper;

    // @PostMapping("signup")
    // public ResponseEntity<AuthModel> signUp(
    //     @RequestPart("model") String model,
    //     @RequestPart(value = "avatar", required = false) MultipartFile avatar) {

    //     SignUpModel signUpModel = null;
    //     try {
    //         signUpModel = mapper.readValue(model, SignUpModel.class);
    //     } catch (Exception e) {
    //         throw new AppException(HttpStatus.BAD_REQUEST, "Dữ liệu không hợp lệ");
    //     }


    @PostMapping("signin")
    public ResponseEntity<AuthModel> signIn(@RequestBody SignInModel signInModel) {
        try {
            Authentication authentication = authConfiguration
                .getAuthenticationManager()
                .authenticate(new UsernamePasswordAuthenticationToken(
                    signInModel.getUsername(), signInModel.getPassword()));

            SecurityContextHolder.getContext().setAuthentication(authentication);

            AppUserDetails userDetails = (AppUserDetails) authentication.getPrincipal();
            AuthModel authModel = new AuthModel(userDetails.getUsername(), userDetails.getEmail(), jwtService.generateToken(userDetails));
            return ResponseEntity.ok(authModel);
        } catch (Exception e) {
            e.printStackTrace();
        }
        throw new AppException(HttpStatus.BAD_REQUEST, "Đăng nhập thất bại", "Hãy thử kiểm tra lại");
    }

    // Change pass by JSON in Body
    @PostMapping("change-password")
    public ResponseEntity<String> changePass(@RequestBody ChangePassModel model,
        HttpServletRequest request) {
        String token = jwtService.getTokenFromRequest(request);
        if (token == null) {
            throw new AppException(HttpStatus.UNAUTHORIZED, "Bạn cần đăng nhập để thực hiện hành động này!");
        }
        Integer uid = jwtService.getUserIdFromToken(token);
        if (uid == null) {
            throw new AppException(HttpStatus.BAD_REQUEST, "Lỗi xác thực", "Vui lòng đăng nhập để kiểm tra lại");
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

    @PostMapping("signup")
    public ResponseEntity<AuthModel> signUp(
        @RequestBody SignUpModel signUpModel) {
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
        User user = new User(signUpModel.getUsername(), signUpModel.getEmail(), passwordEncoder.encode(signUpModel.getPassword()), Role.USER);
        user.setAvatar("default-avatar.png");
        User response = userRepository.save(user);
        AuthModel authModel = new AuthModel(user.getUsername(), 
            user.getEmail(), 
            jwtService.generateToken(AppUserDetails.build(response)));
        return ResponseEntity.ok(authModel);
    }

    @PostMapping("signout")
    public ResponseEntity<String> signOut(HttpServletRequest request) {
        String jwt = jwtService.getTokenFromRequest(request);
        if (jwt == null || !jwtService.validTokenType(jwt)) {
            return ResponseEntity.badRequest()
                .body("Xác thực không hợp lệ");
        }
        Integer uid = jwtService.getUserIdFromToken(jwt);
        User user = userRepository.findById(uid)
                    .orElseThrow(() -> new AppException(HttpStatus.NOT_FOUND, "Không tìm thấy user"));
        SecurityContextHolder.getContext().setAuthentication(null);
        tokenDiedRepository.save(new TokenDied(user, LocalDateTime.now(), jwt));
        return ResponseEntity.ok("Đăng xuất thành công");
    }
}
