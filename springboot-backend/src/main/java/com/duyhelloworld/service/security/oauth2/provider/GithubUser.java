package com.duyhelloworld.service.security.oauth2.provider;

import org.springframework.security.config.oauth2.client.CommonOAuth2Provider;
import org.springframework.security.oauth2.core.user.OAuth2User;

import com.duyhelloworld.service.security.oauth2.OAuth2UserDetail;

import lombok.ToString;

@ToString
public class GithubUser extends OAuth2UserDetail {

    public GithubUser(OAuth2User oAuth2User) {
        super(oAuth2User);
    }

    @Override
    public String getEmail() {
        return getAttribute("email");
    }

    @Override
    public String getAvatar() {
        return getAttribute("avatar_url");
    }

    @Override
    public String getUsername() {
        return getAttribute("login");
    }

    @Override
    public CommonOAuth2Provider getProvider() {
        return CommonOAuth2Provider.GITHUB;
    }

    @Override
    public String getFullname() {
        return getAttribute("name");
    }
}
