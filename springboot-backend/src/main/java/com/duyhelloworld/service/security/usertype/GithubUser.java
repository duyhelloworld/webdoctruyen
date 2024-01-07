package com.duyhelloworld.service.security.usertype;

import org.springframework.security.oauth2.core.user.OAuth2User;

import com.duyhelloworld.service.AppUserDetail;
import com.duyhelloworld.service.security.Provider;

public class GithubUser extends AppUserDetail {

    public GithubUser(OAuth2User oAuth2User) {
        super(oAuth2User, null);
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
    public Provider getProvider() {
        return Provider.GITHUB;
    }

    @Override
    public String getFullname() {
        return getAttribute("name");
    }
}
