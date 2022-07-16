package com.itsanubhav.wordroid4;

import android.app.Application;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.util.Log;

import androidx.appcompat.app.AppCompatDelegate;

import com.facebook.ads.AudienceNetworkAds;
import com.google.android.gms.ads.MobileAds;
import com.itsanubhav.libdroid.WordroidInit;
import com.itsanubhav.libdroid.database.PostDatabase;
import com.itsanubhav.libdroid.database.dao.NotificationDao;
import com.itsanubhav.libdroid.model.notification.Notification;
import com.itsanubhav.libdroid.model.settings.AppSettings;
import com.itsanubhav.wordroid4.others.Utils;
import com.mikepenz.iconics.Iconics;
import com.onesignal.OSNotification;
import com.onesignal.OSNotificationAction;
import com.onesignal.OSNotificationOpenResult;
import com.onesignal.OneSignal;

import org.json.JSONObject;
import org.jsoup.Jsoup;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;

public class MainApplication extends Application {

    public static final String PREF_NAME = "wrdrdshrdprf";
    public static final String PREF_NIGHT_MODE = "night-mode";

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
        super.onCreate();
        configureNightMode();

        new WordroidInit(Config.SITE_URL);

        Iconics.init(getApplicationContext());

        MobileAds.initialize(this, initializationStatus -> {
        });
        AudienceNetworkAds.initialize(this);

        // OneSignal Initialization
        OneSignal.startInit(this)
                .inFocusDisplaying(OneSignal.OSInFocusDisplayOption.Notification)
                .unsubscribeWhenNotificationsAreDisabled(true)
                .setNotificationReceivedHandler(notification -> {
                    DateFormat df = new SimpleDateFormat("EEE, d MMM yyyy HH:mm:ss");
                    String date = df.format(Calendar.getInstance().getTime());
                    Notification noti = new Notification();
                    if (notification.payload.body!=null)
                        noti.setTitle(Jsoup.parse(notification.payload.body).text());
                    if (notification.payload.bigPicture!=null)
                        noti.setImage(notification.payload.bigPicture);
                    noti.setTimestamp(date);
                    JSONObject data = notification.payload.additionalData;
                    String type = data.optString("type");
                    switch (type) {
                        case "post":
                            noti.setType(type);
                            noti.setPostId(data.optInt("value"));
                            noti.setTitle(data.optString("message"));
                            break;
                        case "web":
                            String url = data.optString("value");
                            if (url != null)
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
                    new Thread(() -> {
                        NotificationDao dao = PostDatabase.getAppDatabase(getApplicationContext()).notificationDao();
                        dao.insertNotification(noti);
                    }).start();



                })
                .setNotificationOpenedHandler(result -> {
                    OSNotificationAction.ActionType actionType = result.action.type;
                    JSONObject data = result.notification.payload.additionalData;
                    String type;

                    if (data != null) {
                        type = data.optString("type");
                        switch (type) {
                            case "post": {
                                int postId = data.optInt("value");
                                String postTitle = data.optString("title");

                                Intent intent = new Intent(getApplicationContext(), PostContainerActivity.class);
                                intent.putExtra("postId", postId);
                                intent.putExtra("title", postTitle);
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
                        Log.i("OneSignalExample", "Button pressed with id: " + result.action.actionID);
                })
                .init();
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

    class NotificationReceivedHandler implements OneSignal.NotificationReceivedHandler {
        @Override
        public void notificationReceived(OSNotification notification) {
            DateFormat df = new SimpleDateFormat("EEE, d MMM yyyy HH:mm:ss");
            String date = df.format(Calendar.getInstance().getTime());
            Notification noti = new Notification();
            if (notification.payload.body!=null)
                noti.setTitle(Jsoup.parse(notification.payload.body).text());
            if (notification.payload.bigPicture!=null)
                noti.setImage(notification.payload.bigPicture);
            noti.setTimestamp(date);
            JSONObject data = notification.payload.additionalData;
            String type = data.optString("type");
            switch (type){
                case "post":
                    noti.setType(type);
                    noti.setPostId(data.optInt("value"));
                    break;
                case "web":
                    String url = data.optString("value");
                    if (url!=null)
                        noti.setUrl(url);
                    noti.setType(type);
                    break;
                case "message":
                    String title, message;
                    title = data.optString("title");
                    message = data.optString("message");
                    noti.setType(type);
                    /*noti.setMsgTitle(title);
                    noti.setMsgBody(message);*/
                    break;
            }
            NotificationDao dao = PostDatabase.getAppDatabase(getApplicationContext()).notificationDao();
            dao.insertNotification(noti);

        }
    }

    class NotificationOpenHandler implements OneSignal.NotificationOpenedHandler{

        @Override
        public void notificationOpened(OSNotificationOpenResult result) {
            OSNotificationAction.ActionType actionType = result.action.type;
            JSONObject data = result.notification.payload.additionalData;
            String type;

            if (data != null) {
                type = data.optString("type");
                switch (type) {
                    case "post": {
                        int postId = data.optInt("value");
                        Log.i("OneSignalExample", "customkey set with value: " + postId);
                        Intent intent = new Intent(getApplicationContext(), DetailActivity.class);
                        intent.putExtra("postId", postId);
                        intent.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT | Intent.FLAG_ACTIVITY_NEW_TASK);
                        startActivity(intent);
                        break;
                    }
                    case "web": {
                        String url = data.optString("value");
                        Intent intent;
                        //TODO: Add WebView Activity
                        /*if (Config.OPEN_EXTERNAL_LINKS_IN_APP){
                            intent = new Intent(getApplicationContext(),WebViewActivity.class);
                            intent.putExtra("URL",url);
                        }else {
                            intent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
                        }
                        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        intent.setPackage("com.android.chrome");
                        try {
                            startActivity(intent);
                        } catch (ActivityNotFoundException ex) {
                            intent.setPackage(null);
                            startActivity(intent);
                        }*/
                        break;
                    }
                    case "message": {
                        String title, message;
                        title = data.optString("title");
                        message = data.optString("message");
                        Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                        intent.putExtra("title",title);
                        intent.putExtra("message",message);
                        intent.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT | Intent.FLAG_ACTIVITY_NEW_TASK);
                        startActivity(intent);
                        break;
                    }
                }
            }

            if (actionType == OSNotificationAction.ActionType.ActionTaken)
                Log.i("OneSignalExample", "Button pressed with id: " + result.action.actionID);
        }
    }


}
