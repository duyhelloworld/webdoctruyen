package com.duyhelloworld.service.security;

import java.util.Collection;

import org.springframework.security.config.oauth2.client.CommonOAuth2Provider;
import org.springframework.security.core.GrantedAuthority;

public interface AbstractUserInfo {

    public Integer getUserId();

    public String getEmail();

    public String getAvatar();
    
    public String getUsername();
    
    public CommonOAuth2Provider getProvider();

    public String getFullname();

    public Collection<? extends GrantedAuthority> getAuthorities();
}
