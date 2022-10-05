package com.allexamnotes.app;

import android.app.Application;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.util.Log;

import androidx.appcompat.app.AppCompatDelegate;

import com.facebook.ads.AudienceNetworkAds;
import com.google.android.gms.ads.MobileAds;
import com.allexamnotes.libdroid.WordroidInit;
import com.allexamnotes.libdroid.database.PostDatabase;
import com.allexamnotes.libdroid.database.dao.NotificationDao;
import com.allexamnotes.libdroid.model.notification.Notification;
import com.allexamnotes.libdroid.model.settings.AppSettings;
import com.allexamnotes.app.others.Utils;
import com.mikepenz.iconics.Iconics;
import com.onesignal.OSNotification;
import com.onesignal.OSNotificationAction;
import com.onesignal.OneSignal;

import org.json.JSONObject;
import org.jsoup.Jsoup;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

public class MainApplication extends Application {

    public static final String PREF_NAME = "wrdrdshrdprf";
    public static final String PREF_NIGHT_MODE = "night-mode";
    public static final String ONESIGNAl_APP_ID = "89786ea1-5307-4830-a617-a752d5e28d29";


    private static AppSettings appSettings;
    public static int INT_AD_COUNTER = 0;
    public static int adCounter;

    private boolean nightMode = true;

    public static AppSettings getAppSettings(Context c) {
        if (appSettings==null){
            appSettings = Utils.getSettings(c);
            return appSettings;
        }
        return appSettings;
    }

    @Override
    public void onCreate() {

        if(Config.FORCE_RTL) {
            // Create a new Locale object
            Locale locale = new Locale("ar");
            Locale.setDefault(locale);
            // Create a new configuration object
            Configuration config = new Configuration();
            // Set the locale of the new configuration
            config.locale = locale;
            // Update the configuration of the Accplication context
            getResources().updateConfiguration(
                    config,
                    getResources().getDisplayMetrics()
            );
        }


        super.onCreate();
        configureNightMode();

        SharedPreferences sharedPreferences = getSharedPreferences(Config.defaultSharedPref,MODE_PRIVATE);

        if(Config.TEST_MODE) {
            new WordroidInit(sharedPreferences.getString("test_site_url",Config.SITE_URL));
        }else{
            new WordroidInit(Config.SITE_URL);
        }

        Iconics.init(getApplicationContext());

        MobileAds.initialize(this, initializationStatus -> {});
        AudienceNetworkAds.initialize(this);

        // OneSignal Initialization
        OneSignal.initWithContext(this);
        OneSignal.setAppId(ONESIGNAl_APP_ID);
        OneSignal.unsubscribeWhenNotificationsAreDisabled(true);
        OneSignal.setNotificationOpenedHandler(result -> {
            OSNotificationAction.ActionType actionType = result.getAction().getType();
            JSONObject data = result.getNotification().getAdditionalData();
            String type;

            if (data != null) {
                type = data.optString("type");
                switch (type) {
                    case "post": {
                        int postId = data.optInt("value");
                        String postTitle = data.optString("title");

                        Intent intent = new Intent(getApplicationContext(), DetailActivity.class);
                        intent.putExtra("postId", postId);
                        intent.putExtra("title", postTitle);
                        intent.putExtra("img",result.getNotification().getBigPicture());
                        intent.putExtra("fromNotification",true);
                        intent.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT | Intent.FLAG_ACTIVITY_NEW_TASK);
                        startActivity(intent);
                        break;
                    }
                    case "web": {
                        String url = data.optString("value");
                        Intent intent = new Intent(getApplicationContext(), ContainerActivity.class);
                        intent.putExtra("screen","webview");
                        intent.putExtra("url",url);
                        intent.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT | Intent.FLAG_ACTIVITY_NEW_TASK);
                        startActivity(intent);
                        break;
                    }
                    case "message": {
                        String title, message;
                        title = data.optString("title");
                        message = data.optString("message");
                        Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                        intent.putExtra("msgTitle",title);
                        intent.putExtra("msgBody",message);
                        intent.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT | Intent.FLAG_ACTIVITY_NEW_TASK);
                        startActivity(intent);
                        break;
                    }
                }
            }

            if (actionType == OSNotificationAction.ActionType.ActionTaken)
                Log.i("OneSignalExample", "Button pressed with id: " + result.getAction().getActionId());
        });
        OneSignal.setNotificationWillShowInForegroundHandler(result -> {
            OSNotification notification = result.getNotification();
            DateFormat df = new SimpleDateFormat("EEE, d MMM yyyy HH:mm:ss");
            String date = df.format(Calendar.getInstance().getTime());
            Notification noti = new Notification();
            if (notification.getBody()!=null)
                noti.setTitle(Jsoup.parse(notification.getBody()).text());
            if (notification.getBigPicture()!=null)
                noti.setImage(notification.getBigPicture());
            noti.setTimestamp(date);
            JSONObject data = notification.getAdditionalData();
            String type = data.optString("type");
            switch (type) {
                case "post":
                    noti.setType(type);
                    noti.setPostId(data.optInt("value"));
                    noti.setTitle(data.optString("message"));
                    break;
                case "web":
                    String url = data.optString("value");
                    noti.setUrl(url);
                    noti.setType(type);
                    break;
                case "message":
                    String title, message;
                    title = data.optString("title");
                    message = data.optString("message");
                    noti.setType(type);
                    noti.setMsgTitle(title);
                    noti.setMsgBody(message);
                    break;
            }
            Log.e("Notification Rec.",noti.toString());
            new Thread(() -> {
                NotificationDao dao = PostDatabase.getAppDatabase(getApplicationContext()).notificationDao();
                dao.insertNotification(noti);
            }).start();
        });
    }

    private void configureNightMode(){
        SharedPreferences sharedPreferences = getSharedPreferences(Config.defaultSharedPref,MODE_PRIVATE);
        int nightMode = sharedPreferences.getInt("dark_mode",0);
        switch (nightMode){
            case 1:
                AppCompatDelegate.setDefaultNightMode(
                        AppCompatDelegate.MODE_NIGHT_YES);
                break;
            case 2:
                AppCompatDelegate.setDefaultNightMode(
                        AppCompatDelegate.MODE_NIGHT_FOLLOW_SYSTEM);
                break;
            default:
                AppCompatDelegate.setDefaultNightMode(
                        AppCompatDelegate.MODE_NIGHT_NO);
        }
    }

}
