package com.allexamnotes.libdroid;

public class WordroidInit {
    private static String SITE_URL;

    public WordroidInit(String url){
        SITE_URL = url;
    }

    public static String getSiteUrl() {
        return SITE_URL;
    }
}
