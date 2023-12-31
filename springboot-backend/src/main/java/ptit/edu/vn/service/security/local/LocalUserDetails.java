package ptit.edu.vn.service.security.local;

import java.util.Collection;
import java.util.List;

import org.springframework.security.config.oauth2.client.CommonOAuth2Provider;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import ptit.edu.vn.entity.User;
import ptit.edu.vn.service.security.AbstractUserInfo;

@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class LocalUserDetails implements UserDetails, AbstractUserInfo {
    private User user;

    public static LocalUserDetails build(User user) {
        return new LocalUserDetails(user);
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return List.of(new SimpleGrantedAuthority(user.getRole().name()));
    }

    @Override
    public String getPassword() {
        return user.getPassword();
    }

    @Override
    public String getUsername() {
        return user.getUsername();
    }

    @Override
    public boolean isAccountNonExpired() {
        return user.isAccountNonExpired();
    }

    @Override
    public boolean isAccountNonLocked() {
        return user.isAccountNonLocked();
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return user.isCredentialsNonExpired();
    }

    @Override
    public boolean isEnabled() {
        return user.isEnable();
    }

    @Override
    public String getEmail() {
        return user.getEmail();
    }

    @Override
    public String getAvatar() {
        return user.getAvatar();
    }

    @Override
    public Integer getId() {
        return user.getId();
    }

    @Override
    public CommonOAuth2Provider getProvider() {
        return null;
    }

    @Override
    public String getFullname() {
        return user.getFullname();
    }
}
