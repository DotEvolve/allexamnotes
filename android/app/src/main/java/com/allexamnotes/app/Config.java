package com.allexamnotes.app;

public class Config {

    public static String SITE_URL = "https://www.allexamnotes.com";

    public static final String chanelID = "UCxNnKqtwiTaX5WdsqsVOIlw";

    public static final String YT_API_KEY = "AIzaSyAkamPp_DwUlvmsbEPI_g7kdKaaaEPND0I";

    public static final String YT_LATEST_CHANEL_ID = "UCxNnKqtwiTaX5WdsqsVOIlw";

    public static final boolean LOAD_CATEGORIES_WITH_BACKGROUND_IMGS = true;
    // When marked true, app will show the categories as a grid with background images instead of the regular list.
    // The background images should be set from WordPress Dashboard > Posts > Categories > Edit Categories > Background Image
    // App will show a default image when there is no category is available.

    public static final boolean AUTO_LOAD_SMART_IMAGES = true;
    //Background images will be loaded automatically based on the name of category. Categories having no image will use this feature.

    public static final int NO_OF_RELATED_POSTS_TO_LOAD = 3;
    /*
    * Main screen will be shown after the latest settings fetched from the website.
    * The loading time may vary depending on server. Users may have to wait several seconds (2-5) before they see the content.
    **/


    public static final boolean DISABLE_SETTINGS_CACHE = false;

    //Experimental feature
    public static final boolean TEST_MODE = false;

    public static final boolean FORCE_CACHE = false;

    public static final boolean USE_DOWNLOAD_IMAGE_FEATURE = true;

    public static final boolean FORCE_RTL = false;

    public static final boolean ALLOW_USERS_TO_ADD_CATEGORIES = false;

    //If you enable this, make sure you have installed the JWT plugin and is working
    public static final boolean ENABLE_WORDPRESS_LOGIN = true;



    //Other Settings. Please do not change anything below this
    public static final String defaultSharedPref = "wordroid4_"+SITE_URL.replace("/","").replace(":","");

    //this setting is not moved to the wordpress dashboard. Any changes made here won't be used in the app
    public static boolean FB_ADS = true;
    public static boolean ADMOB_ADS = true;

}
