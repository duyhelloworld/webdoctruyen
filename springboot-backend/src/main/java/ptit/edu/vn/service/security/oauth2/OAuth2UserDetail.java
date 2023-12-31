package ptit.edu.vn.service.security.oauth2;

import java.util.Collection;
import java.util.Map;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.oauth2.core.user.OAuth2User;

import ptit.edu.vn.service.security.AbstractUserInfo;

public abstract class OAuth2UserDetail implements AbstractUserInfo, OAuth2User {
    private OAuth2User oAuth2User;
    private Integer userId;

    public OAuth2UserDetail(OAuth2User oAuth2User) {
        this.oAuth2User = oAuth2User;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }

    @Override
    public Map<String, Object> getAttributes() {
        return oAuth2User.getAttributes();
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return oAuth2User.getAuthorities();
    }

    @Override
    public String getName() {
        return oAuth2User.getName();
    }

    @Override
    public Integer getId() {
        return userId;
    }
}
