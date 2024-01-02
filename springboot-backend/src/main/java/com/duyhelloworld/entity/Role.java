package com.duyhelloworld.entity;

import java.util.Set;

public enum Role {
    USER {
        @Override
        public Set<Permission> getPermissions() {
            return Set.of(
                Permission.READ_MANGA,
                Permission.RATE_MANGA,
                Permission.WRITE_COMMENT,
                Permission.REPORT_COMMENT,
                Permission.UPDATE_PROFILE,
                Permission.CHANGE_PASSWORD
            );
        }
    },
    SUPERADMIN {
        @Override
        public Set<Permission> getPermissions() {
            return Set.of(
                Permission.READ_MANGA,       Permission.RATE_MANGA,
                Permission.MANAGE_MANGA,     Permission.READ_GENRE,
                Permission.MANAGE_GENRE,     Permission.WRITE_COMMENT,
                Permission.REPORT_COMMENT,   Permission.MANAGE_COMMENT,
                Permission.UPDATE_PROFILE,   Permission.MANAGE_USERS,
                Permission.CHANGE_PASSWORD
            );
        }
    },
    ADMIN {
        @Override
        public Set<Permission> getPermissions() {
            return Set.of(
                Permission.READ_MANGA,       Permission.RATE_MANGA,
                Permission.MANAGE_MANGA,     Permission.READ_GENRE,
                Permission.MANAGE_GENRE,     Permission.WRITE_COMMENT,
                Permission.REPORT_COMMENT,   Permission.MANAGE_COMMENT,
                Permission.UPDATE_PROFILE,   Permission.MANAGE_USERS,
                Permission.REQUEST_ACCOUNT_DELETION,  Permission.CHANGE_PASSWORD
            );
        }
    };

    public abstract Set<Permission> getPermissions();
}
