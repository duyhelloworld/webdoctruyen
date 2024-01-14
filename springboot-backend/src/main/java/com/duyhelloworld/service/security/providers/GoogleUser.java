package com.duyhelloworld.service.security.providers;

import java.util.Optional;

import org.springframework.security.oauth2.core.user.OAuth2User;

import com.duyhelloworld.service.AppUserDetail;
import com.duyhelloworld.service.security.Provider;

public class GoogleUser extends AppUserDetail {
    
    public GoogleUser(OAuth2User oAuth2User) {
        super(oAuth2User, null);
    }

    @Override
    public Provider getProvider() {
        return Provider.GOOGLE;
    }

    @Override
    public String getEmail() {
        return getAttribute("email");
    }

    @Override
    public String getAvatar() {
        return getAttribute("picture");
    }

    @Override
    public String getUsername() {
        String email = Optional.of(getAttribute("email")).get().toString();
        return email.substring(0, email.indexOf("@"));
    }

    @Override
    public String getFullname() {
        return Optional.ofNullable(getAttribute("name"))
            .map(Object::toString)
            .orElse(getAttribute("given_name") + " " + getAttribute("family_name"));
    }
}
