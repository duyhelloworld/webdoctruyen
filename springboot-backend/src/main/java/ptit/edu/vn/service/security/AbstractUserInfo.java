package ptit.edu.vn.service.security;

import org.springframework.security.config.oauth2.client.CommonOAuth2Provider;

public interface AbstractUserInfo {
    public Integer getId();

    public String getEmail();

    public String getAvatar();
    
    public String getUsername();
    
    public CommonOAuth2Provider getProvider();

    public String getFullname();
}
