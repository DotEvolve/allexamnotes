package com.allexamnotes.app.others;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.net.ConnectivityManager;
import android.widget.Toast;

import com.google.gson.Gson;
import com.allexamnotes.libdroid.model.settings.AppSettings;
import com.allexamnotes.app.BuildConfig;
import com.allexamnotes.app.Config;

import java.io.IOException;
import java.io.InputStream;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.Random;


public class Utils {

    public static boolean isNetworkConnected(Context context) {
        ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);

        return cm.getActiveNetworkInfo() != null;
    }

    public static void showDebugToast(String msg, Context context){
        if( BuildConfig.BUILD_TYPE.equalsIgnoreCase("debug")) {
            Toast.makeText(context,msg,Toast.LENGTH_SHORT).show();
        }
    }

    public static String parseDate(String dateString){
        DateFormat format = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss", Locale.ENGLISH);
        try {
            Date date = format.parse(dateString);
            format = new SimpleDateFormat("MMM dd, yyyy",Locale.ENGLISH);
            String s = format.format(date);
            return s;
        } catch (ParseException e) {
            return dateString;
        }
    }

    public static int generateRandomColor() {
        final Random mRandom = new Random(System.currentTimeMillis());
        // This is the base color which will be mixed with the generated one
        final int baseColor = Color.parseColor("#1976D2");
        final int baseRed = Color.red(baseColor);
        final int baseGreen = Color.green(baseColor);
        final int baseBlue = Color.blue(baseColor);
        final int red = (baseRed + mRandom.nextInt(256)) / 2;
        final int green = (baseGreen + mRandom.nextInt(256)) / 2;
        final int blue = (baseBlue + mRandom.nextInt(256)) / 2;
        return Color.rgb(red, green, blue);
    }

    public static int getTextColorByBackground(int color){
        double darkness = 1-(0.299*Color.red(color) + 0.587*Color.green(color) + 0.114*Color.blue(color))/255;
        if(darkness<0.5){
            return Color.rgb(0,0,0); // It's a light color
        }else{
            return Color.rgb(255,255,255); // It's a dark color
        }
    }

    public static AppSettings getSettings(Context context){
        SharedPreferences sharedPreferences = context.getSharedPreferences(Config.defaultSharedPref,Context.MODE_PRIVATE);
        String json = sharedPreferences.getString("settings",null);
        Gson gson = new Gson();
        return gson.fromJson(json, AppSettings.class);
    }

    public static String loadJSON(Context context,String filename) {
        String json = null;
        try {
            InputStream is = context.getAssets().open(filename);
            int size = is.available();
            byte[] buffer = new byte[size];
            is.read(buffer);
            is.close();
            json = new String(buffer, "UTF-8");
        } catch (IOException ex) {
            ex.printStackTrace();
            return null;
        }
        return json;
    }

}
