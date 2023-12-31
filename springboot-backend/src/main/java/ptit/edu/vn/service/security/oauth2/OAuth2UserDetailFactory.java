package ptit.edu.vn.service.security.oauth2;

import org.springframework.http.HttpStatus;
import org.springframework.security.config.oauth2.client.CommonOAuth2Provider;
import org.springframework.security.oauth2.core.user.OAuth2User;

import ptit.edu.vn.exception.AppException;
import ptit.edu.vn.service.security.oauth2.provider.FacebookUser;
import ptit.edu.vn.service.security.oauth2.provider.GoogleUser;

public abstract class OAuth2UserDetailFactory {
    public static OAuth2UserDetail getOAuth2UserDetail(OAuth2User oAuth2User, String provider) {
        CommonOAuth2Provider appProvider = CommonOAuth2Provider.valueOf(provider.toUpperCase());
        switch (appProvider) {
            case GOOGLE:
                return new GoogleUser(oAuth2User);
            case FACEBOOK:
                return new FacebookUser(oAuth2User);
            default:
                throw new AppException(HttpStatus.BAD_REQUEST, 
                "Dịch vụ này không hợp lệ / Ứng dụng chưa hỗ trợ " + provider);
        }
    }
}
