package ptit.edu.vn.service.security.oauth2.provider;

import java.util.Optional;

import org.springframework.security.config.oauth2.client.CommonOAuth2Provider;
import org.springframework.security.oauth2.core.user.OAuth2User;

import ptit.edu.vn.service.security.oauth2.OAuth2UserDetail;

public class GoogleUser extends OAuth2UserDetail {
    public GoogleUser(OAuth2User oAuth2User) {
        super(oAuth2User);
    }

    @Override
    public CommonOAuth2Provider getProvider() {
        return CommonOAuth2Provider.GOOGLE;
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
        String email = getAttribute("email");
        return email.substring(0, email.indexOf("@"));
    }

    @Override
    public String getFullname() {
        return Optional.ofNullable(getAttribute("name"))
            .map(Object::toString)
            .orElse(getAttribute("given_name") + " " + getAttribute("family_name"));
    }
}
