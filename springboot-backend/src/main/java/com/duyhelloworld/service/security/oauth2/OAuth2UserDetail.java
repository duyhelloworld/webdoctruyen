package com.duyhelloworld.service.security.oauth2;

import java.util.Collection;
import java.util.List;
import java.util.Map;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.oauth2.core.user.OAuth2User;

import com.duyhelloworld.entity.Role;
import com.duyhelloworld.service.security.AbstractUserInfo;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public abstract class OAuth2UserDetail implements AbstractUserInfo, OAuth2User {
    private OAuth2User oAuth2User;

    private Role role;
    
    private Integer userId;

    public OAuth2UserDetail(OAuth2User oAuth2User) {
        this.oAuth2User = oAuth2User;
    }

    @Override
    public Map<String, Object> getAttributes() {
        return oAuth2User.getAttributes();
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return List.of(new SimpleGrantedAuthority(role.name()));
    }

    @Override
    public String getName() {
        return oAuth2User.getName();
    }
}
