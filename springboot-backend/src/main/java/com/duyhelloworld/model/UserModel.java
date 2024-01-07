package com.duyhelloworld.model;

import com.duyhelloworld.entity.User;

import lombok.Data;

@Data
public class UserModel {
    private Integer id;
    private String username;
    private String email;
    private String fullname;

    public static UserModel convert(User user) {
        UserModel userModel = new UserModel();
        userModel.setId(user.getId());
        userModel.setUsername(user.getUsername());
        userModel.setEmail(user.getEmail());
        userModel.setFullname(user.getFullname());
        return userModel;
    }
}
