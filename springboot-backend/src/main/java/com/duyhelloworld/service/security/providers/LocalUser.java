package com.duyhelloworld.service.security.providers;

import com.duyhelloworld.entity.User;
import com.duyhelloworld.service.AppUserDetail;
import com.duyhelloworld.service.security.Provider;

public class LocalUser extends AppUserDetail {

    public LocalUser(User user) {
        super(null, user);
    }

    @Override
    public String getUsername() {
        return super.getUser().getUsername();
    }

    @Override
    public Provider getProvider() {
        return Provider.LOCAL;
    }

    @Override
    public String getEmail() {
        return super.getUser().getEmail();
    }

    @Override
    public String getAvatar() {
        return super.getUser().getAvatar();
    }

    @Override
    public String getFullname() {
        return super.getUser().getFullname();
    }
}
