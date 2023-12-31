package ptit.edu.vn.service.security.oauth2.provider;

import org.springframework.security.config.oauth2.client.CommonOAuth2Provider;
import org.springframework.security.oauth2.core.user.OAuth2User;

import ptit.edu.vn.service.security.oauth2.OAuth2UserDetail;

public class FacebookUser extends OAuth2UserDetail {
    public FacebookUser(OAuth2User oAuth2User) {
        super(oAuth2User);
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
    public CommonOAuth2Provider getProvider() {
        return CommonOAuth2Provider.FACEBOOK;
    }

    @Override
    public String getFullname() {
        return getAttribute("name");
    }
    
}
