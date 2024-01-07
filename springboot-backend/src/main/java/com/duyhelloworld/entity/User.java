package com.duyhelloworld.entity;

import java.util.List;

import com.duyhelloworld.service.security.Provider;

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
import lombok.ToString;

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

    @Column(length = 100)
    private String email;

    // pass sẽ được hash trước khi vào đây
    @ToString.Exclude
    private String password;

    private boolean isEnabled;

    private boolean isAccountNonExpired;

    private boolean isAccountNonLocked;

    private boolean isCredentialsNonExpired;
    
    @Enumerated(EnumType.STRING)
    private Provider provider;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Role role;

    public User(String username, String email, String password, String avatar, String fullname, Role role) {
        this.username = username;
        this.email = email;
        this.password = password;
        this.role = role;
        this.fullname = fullname;
        this.avatar = avatar;
        this.isEnabled = true;
        this.isAccountNonExpired = true;
        this.isAccountNonLocked = true;
        this.isCredentialsNonExpired = true;
    }

    @ToString.Exclude
    @OneToMany(mappedBy = "user")
    private List<Comment> Comments;

    @ToString.Exclude
    @OneToMany(mappedBy = "user")
    private List<Rating> Ratings;

    @ToString.Exclude
    @OneToMany(mappedBy = "user", fetch = FetchType.EAGER)
    private List<TokenDied> TokenDieds;
}
