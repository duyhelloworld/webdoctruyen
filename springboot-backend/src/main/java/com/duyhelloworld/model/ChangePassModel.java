package com.duyhelloworld.model;

import lombok.Data;

@Data
public class ChangePassModel {
    private String oldPassword;
    private String newPassword;
    private String confirmPassword;
}
