package com.itsanubhav.wordroid4;

public class Config {

    public static final String SITE_URL = "http://dev.itsanubhav.com";

    public static final String chanelID = "UCNhT2txZHDfeIS0g7ZK7uRg";

    public static final String YT_API_KEY = "AIzaSyCXuIcy4NSAkHxwLqPYt0DF41Bn-n6HxVc";

    public static final String YT_LATEST_CHANEL_ID = "UUNhT2txZHDfeIS0g7ZK7uRg";

    public static final String defaultSharedPref = "wordroid4_"+SITE_URL.replace("/","").replace(":","");

    public static final boolean USE_DOWNLOAD_IMAGE_FEATURE = true;

    public static final boolean FORCE_RTL = false;

    //If you enable this, make sure you have installed the JWT plugin and is working
    public static final boolean ENABLE_WORDPRESS_LOGIN = false;

}
