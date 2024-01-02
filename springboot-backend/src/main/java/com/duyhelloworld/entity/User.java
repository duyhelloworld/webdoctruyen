package com.duyhelloworld.entity;

import java.util.List;

import org.springframework.security.config.oauth2.client.CommonOAuth2Provider;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Entity
@NoArgsConstructor
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    
    @Column(length = 100, nullable = false, unique = true)
    private String username;
    
    private String fullname;

    @Column(nullable = false)
    private String avatar;

    @Column(unique = true)
    private String email;

    // pass sẽ được hash trước khi vào đây
    private String password;

    private boolean isEnable;

    private boolean isAccountNonExpired;

    private boolean isAccountNonLocked;

    private boolean isCredentialsNonExpired;
    
    @Enumerated(EnumType.STRING)
    private CommonOAuth2Provider provider;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Role role;

    public User(String username, String email, String password, String fullname, String avatar, Role role) {
        this.username = username;
        this.email = email;
        this.password = password;
        this.role = role;
        this.fullname = fullname;
        this.avatar = avatar;
        this.isEnable = true;
        this.isAccountNonExpired = true;
        this.isAccountNonLocked = true;
        this.isCredentialsNonExpired = true;
    }
    
    @OneToMany(mappedBy = "user", fetch = FetchType.LAZY)
    private List<Comment> Comments;

    @OneToMany(mappedBy = "user", fetch = FetchType.LAZY)
    private List<Rating> Ratings;

    @OneToMany(mappedBy = "user", fetch = FetchType.EAGER)
    private List<TokenDied> TokenDieds;
}
