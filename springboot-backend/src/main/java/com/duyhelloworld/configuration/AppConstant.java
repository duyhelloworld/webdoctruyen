package com.duyhelloworld.configuration;

import org.springframework.http.MediaType;

public class AppConstant {
    public static final int PAGE_SIZE = 10;

    public static final MediaType USER_AVATAR_FILE_EXTENSION = MediaType.IMAGE_PNG;

    public static final String ROOT_DIR 
        = System.getProperty("user.dir") + "/src/main/resources/static/";

    public static final String BOOK_DIR = ROOT_DIR + "mangas";
    public static final String COVERIMAGE_DIR = ROOT_DIR + "coverimages";
    public static final String AVATAR_DIR = ROOT_DIR + "avatars";
    public static final String DEFAULT_COVERIMAGE = "default-coverimage.png";
    public static final String DEFAULT_AVATAR = "default-avatar.png";
}
