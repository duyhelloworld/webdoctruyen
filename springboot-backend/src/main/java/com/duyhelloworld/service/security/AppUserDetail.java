package com.duyhelloworld.service.security;

import java.util.Collection;
import java.util.List;
import java.util.Map;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.oauth2.core.user.OAuth2User;

import com.duyhelloworld.entity.User;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@ToString
public abstract class AppUserDetail implements OAuth2User, UserDetails {
    
    private OAuth2User oAuth2User;

    @Getter
    @Setter
    private User user;

    public AppUserDetail(OAuth2User oAuth2User, User user) {
        this.oAuth2User = oAuth2User;
        this.user = user;
    }

    // For OAuth2
    public abstract Provider getProvider();

    public abstract String getEmail();
    
    public abstract String getAvatar();
    
    public abstract String getFullname();

    @Override
    public Map<String, Object> getAttributes() {
        return oAuth2User.getAttributes();
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return List.of(new SimpleGrantedAuthority(user.getRole().name()));
    }

    @Override
    public String getName() {
        return oAuth2User.getName();
    }

    @Override
    public String getPassword() {
        return user.getPassword();
    }

    @Override
    public boolean isAccountNonExpired() {
        return user.isAccountNonExpired();
    }

    @Override
    public boolean isAccountNonLocked() {
        return user.isAccountNonExpired();
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return user.isCredentialsNonExpired();
    }

    @Override
    public boolean isEnabled() {
        return user.isEnabled();
    }
}
