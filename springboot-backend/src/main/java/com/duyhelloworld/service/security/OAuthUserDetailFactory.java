package com.duyhelloworld.service.security;

import org.springframework.http.HttpStatus;
import org.springframework.security.oauth2.core.user.OAuth2User;

import com.duyhelloworld.exception.AppException;
import com.duyhelloworld.service.security.providers.FacebookUser;
import com.duyhelloworld.service.security.providers.GithubUser;
import com.duyhelloworld.service.security.providers.GoogleUser;

public abstract class OAuthUserDetailFactory {
    public static AppUserDetail create(Provider provider, OAuth2User oAuth2User) {
        switch (provider) {
            case GOOGLE:
                return new GoogleUser(oAuth2User);
            case FACEBOOK:
                return new FacebookUser(oAuth2User);
            case GITHUB:
                return new GithubUser(oAuth2User);
            default:
                throw new AppException(HttpStatus.BAD_REQUEST, "Không hỗ trợ đăng nhập bằng " + provider);
        }
    }
}
