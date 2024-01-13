package com.duyhelloworld.configuration;

import org.springframework.http.MediaType;
import org.springframework.lang.NonNull;

public class AppConstant {
    public static final int PAGE_SIZE = 10;

    @NonNull
    public static final MediaType USER_AVATAR_FILE_TYPE = MediaType.valueOf("image/png");
    
    @NonNull 
    public static final MediaType BOOK_COVERIMAGE_FILE_TYPE = MediaType.valueOf("image/jpg");
    
    @NonNull 
    public static final MediaType CHAPTER_FILE_TYPE = MediaType.valueOf("image/jpg");
    
    public static final String ROOT_DIR 
        = System.getProperty("user.dir") + "/src/main/resources/static/";

    public static final String BOOK_DIR = ROOT_DIR + "mangas";
    public static final String COVERIMAGE_DIR = ROOT_DIR + "coverimages";
    public static final String AVATAR_DIR = ROOT_DIR + "avatars";
    public static final String DEFAULT_COVERIMAGE = "default-coverimage.png";
    public static final String DEFAULT_AVATAR = "default-avatar.png";
}
