package com.allexamnotes.app;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.TextView;
import android.widget.Toast;


import com.google.gson.Gson;
import com.allexamnotes.libdroid.listeners.SettingsListener;
import com.allexamnotes.libdroid.model.settings.AppSettings;
import com.allexamnotes.libdroid.repo.SettingsRepo;

public class SplashActivity extends AppCompatActivity {

    Uri data;
    SharedPreferences sharedPreferences;
    SharedPreferences.Editor  editor;
    TextView appnametext;
    boolean isNewSetting = false;

    private static final String INTRO_SCREEN_CODE = "intro_screen";

    private static final int REQUEST_WELCOME_SCREEN_RESULT = 13;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        if (Config.FORCE_RTL) {
            getWindow().getDecorView().setLayoutDirection(View.LAYOUT_DIRECTION_RTL);
        }
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        if (getIntent()!=null){
            isNewSetting = getIntent().getBooleanExtra("isnewsettings",false);
        }

        sharedPreferences = getSharedPreferences(Config.defaultSharedPref,MODE_PRIVATE);

        String url = sharedPreferences.getString("test_site_url",null);

        if(Config.TEST_MODE&&url==null){
            Toast.makeText(getApplicationContext(),"Running in test mode",Toast.LENGTH_SHORT).show();
            startActivity(new Intent(getApplicationContext(),WordroidTestActivity.class));
            finish();
            return;
        }


        if(isNetworkAvailable()){
            if (getIntent().getData()!= null) {
                data = getIntent().getData();
                if (data != null) {
                    //getSlug(data);
                    getSimpleSlug(data);
                    //finish();
                }
            }else {
                Window window = getWindow();
                window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
                window.setStatusBarColor(getResources().getColor(R.color.colorPrimary));
                boolean b = sharedPreferences.getBoolean("setting_saved", false);
                if (b) {
                    if(Config.DISABLE_SETTINGS_CACHE) {
                        fetchSettings(true);
                    }else {
                        fetchSettings(false);
                        goToMainActivity();
                    }
                } else {
                    fetchSettings(true);
                }
            }
        }else {
            Intent intent = new Intent(getApplicationContext(),ContainerActivity.class);
            intent.putExtra("screen","savedPosts");
            intent.putExtra("title","Saved Posts");
            startActivity(intent);
            finish();
        }

    }

    private void fetchSettings(boolean navigate){
        SettingsRepo settingsRepo = new SettingsRepo();
        settingsRepo.getSettings(getApplicationContext());
        settingsRepo.setListener(new SettingsListener() {
            @Override
            public void onSuccessful(AppSettings settings) {
                editor = sharedPreferences.edit();
                if(settings.getSettings().getAdsType().equals("fb")){
                    Config.ADMOB_ADS = false;
                    Config.FB_ADS = true;
                }else if(settings.getSettings().getAdsType().equals("admob")){
                    Config.ADMOB_ADS = true;
                    Config.FB_ADS = false;
                }else{
                    Config.ADMOB_ADS = false;
                    Config.FB_ADS = false;
                }
                editor.putBoolean("setting_saved", true);
                editor.apply();
                saveSettings(settings);
                if (data == null) {
                    try {
                        if (MainApplication.getAppSettings(getApplicationContext()).getSettings().isAppIntro()) {
                            if (!sharedPreferences.getBoolean("intro_shown", false)) {
                                startActivity(new Intent(getApplicationContext(), IntroActivity.class));
                                finish();
                            } else {
                                if (navigate)
                                    goToMainActivity();
                            }
                        } else {
                            if (navigate)
                                goToMainActivity();
                        }
                    }catch (Exception e){
                        if (navigate)
                            goToMainActivity();
                    }
                }
            }

            @Override
            public void onFaliure(String errorMsg) {
                Toast.makeText(getApplicationContext(), errorMsg, Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void saveSettings(AppSettings settings){
        editor = sharedPreferences.edit();
        Gson gson = new Gson();
        String settingJson = gson.toJson(settings);
        editor.putString("settings",settingJson);
        editor.apply();
    }

    private void retrieveSettings(){
        String json = sharedPreferences.getString("settings",null);
        Gson gson = new Gson();
        AppSettings settings = gson.fromJson(json,AppSettings.class);
    }


    private void goToMainActivity(){
        startActivity(new Intent(getApplicationContext(), MainActivity.class));
        finish();
    }


    /*private void getSlug(Uri url) {

        Toast.makeText(getApplicationContext(), "In getSLug", Toast.LENGTH_SHORT).show();
        String link = url.toString();
        if (url.toString().equals(Config.SITE_URL) || url.toString().equals(Config.SITE_URL + "/")) {
            Intent intent = new Intent(this, MainActivity.class);
            startActivity(intent);
        } else if (url.getQueryParameter("p") != null) {
            String id = url.getQueryParameter("p");
            if (id != null) {
                Intent intent = new Intent(this, DetailActivity.class);
                intent.putExtra(DetailActivity.ARG_POST_URL, link);
                intent.putExtra(DetailActivity.ARG_POSTID, id);
                startActivity(intent);
            }
        } else if (url.toString().contains("/category/")) {
            if (url.getLastPathSegment() != null) {
                Intent intent = new Intent(this, PostListActivity.class);
                intent.putExtra("category", url.getLastPathSegment());
                startActivity(intent);
            }
        } else {
            String slug = url.getLastPathSegment();
            if (url.toString().contains(".html")) {
                Intent intent = new Intent(this, DetailActivity.class);
                intent.putExtra(DetailActivity.ARG_POST_URL, link);
                intent.putExtra(DetailActivity.ARG_POST_SLUG, slug.replace(".html", ""));
                startActivity(intent);
            } else if (url.toString().matches("-?\\d+")) {
                Toast.makeText(getApplicationContext(), "1st", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(this, DetailActivity.class);
                intent.putExtra(DetailActivity.ARG_POST_URL, link);
                intent.putExtra(DetailActivity.ARG_POST_SLUG, slug);
                startActivity(intent);
            } else {
                Toast.makeText(getApplicationContext(), "2nd", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(this, DetailActivity.class);
                intent.putExtra(DetailActivity.ARG_POST_URL, link);
                intent.putExtra(DetailActivity.ARG_POST_SLUG, slug);
                startActivity(intent);
            }
        }
    }*/

    private void getSimpleSlug(Uri uri) {
        //This will grab the slug if it is at the last segment of the URL i.e
        //https://dev.itsanubhav.com/sample-post/
        //https://dev.itsanubhav.com/2020/01/11/sample-post/
        //https://dev.itsanubhav.com/2020/01/sample-post/

        String slug = uri.getLastPathSegment();
        Intent intent = new Intent(this, PostContainerActivity.class);
        intent.putExtra("url", uri.toString());
        intent.putExtra("slug", slug);
        startActivity(intent);
        finish();
    }

    private boolean isNetworkAvailable() {
        ConnectivityManager connectivityManager
                = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
    }

}
