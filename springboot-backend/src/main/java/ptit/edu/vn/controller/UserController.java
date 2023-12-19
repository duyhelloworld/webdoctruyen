package ptit.edu.vn.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import ptit.edu.vn.model.UserModel;
import ptit.edu.vn.repository.UserRepository;
import ptit.edu.vn.service.file.FileService;


@RestController
@RequestMapping("/api/user")
public class UserController {

    @Autowired 
    private FileService fileService;

    @Autowired
    private UserRepository userRepository;

    @GetMapping
    public ResponseEntity<Resource> getAvatar(@RequestParam("avatar") String avatar) {            
        return ResponseEntity.ok()
        .contentType(MediaType.IMAGE_PNG)
        .body(fileService.getAvatar(avatar));
    }

    @GetMapping("all")
    public ResponseEntity<List<UserModel>> getAll()  {
        return ResponseEntity.ok()
            .body(userRepository.findAll().stream().map(user -> {
                UserModel userModel = new UserModel();
                userModel.setId(user.getId());
                userModel.setUsername(user.getUsername());
                userModel.setEmail(user.getEmail());
                userModel.setFullName(user.getFullName());
                return userModel;
            }).toList());
    }
}
