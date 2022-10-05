package com.allexamnotes.app;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputLayout;
import com.allexamnotes.libdroid.listeners.SettingsListener;
import com.allexamnotes.libdroid.model.settings.AppSettings;
import com.allexamnotes.libdroid.repo.SettingsRepo;

public class WordroidTestActivity extends AppCompatActivity {

    MaterialButton test,finish;
    ImageView pluginCheck,pluginCancel,settingCheck,settingCancel;
    TextView pluginText,settingText,bNavItems,navDrawerItems,urlView;
    ProgressBar progressBar;
    LinearLayout plugin,setting,drawer,bnav;
    SharedPreferences sharedPreferences;
    SharedPreferences.Editor editor;
    String domain;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_wordroid_test);

        sharedPreferences = getSharedPreferences(Config.defaultSharedPref,MODE_PRIVATE);

        TextInputLayout layout = findViewById(R.id.site);
        test = findViewById(R.id.test);
        finish = findViewById(R.id.finish);

        pluginText = findViewById(R.id.pluginText);
        settingText = findViewById(R.id.settingText);
        bNavItems = findViewById(R.id.bNavItems);
        navDrawerItems = findViewById(R.id.navDrawerItems);
        urlView = findViewById(R.id.url);

        pluginCheck = findViewById(R.id.pluginCheck);
        pluginCancel = findViewById(R.id.pluginFailed);
        settingCheck = findViewById(R.id.settingCheck);
        settingCancel = findViewById(R.id.settingFailed);

        progressBar = findViewById(R.id.progress);

        plugin = findViewById(R.id.plugin);
        setting = findViewById(R.id.setting);
        drawer = findViewById(R.id.sidenav);
        bnav = findViewById(R.id.bnav);


        test.setOnClickListener(view -> {
            hideSoftKeyboard(this);
            domain = layout.getEditText().getText().toString();
            String url = domain + "/wp-json/wordroid4/v4/settings";
            urlView.setText("Pinging URL "+url);
            fetchSettings(url);
        });

        finish.setOnClickListener(view -> {
            editor = sharedPreferences.edit();
            editor.putString("test_site_url",domain);
            editor.apply();

            Toast.makeText(getApplicationContext(),"Settings Saved. Restart the application to see the changes",Toast.LENGTH_SHORT).show();
        });

    }

    private void fetchSettings(String url){
        progressBar.setVisibility(View.VISIBLE);
        SettingsRepo settingsRepo = new SettingsRepo();
        settingsRepo.getSettings(getApplicationContext(),url);
        settingsRepo.setListener(new SettingsListener() {
            @Override
            public void onSuccessful(AppSettings settings) {
                pluginCheck.setVisibility(View.VISIBLE);
                plugin.setVisibility(View.VISIBLE);
                if(settings.getSettings()!=null){

                    setting.setVisibility(View.VISIBLE);
                    settingCheck.setVisibility(View.VISIBLE);

                    drawer.setVisibility(View.VISIBLE);
                    navDrawerItems.setText(settings.getSettings().getNavDrawer().getItems().size() + " Navigation Drawer Items Found");

                    bnav.setVisibility(View.VISIBLE);
                    bNavItems.setText(settings.getSettings().getBottomNavigation().getItems().size() + " Bottom Navigation Items Found");

                    progressBar.setVisibility(View.GONE);

                    finish.setEnabled(true);
                }
            }

            @Override
            public void onFaliure(String errorMsg) {
                progressBar.setVisibility(View.GONE);

                pluginCancel.setVisibility(View.VISIBLE);
                pluginText.setText("Plugin not found");
                plugin.setVisibility(View.VISIBLE);

                setting.setVisibility(View.VISIBLE);
                pluginText.setText("Settings not found");
                settingCancel.setVisibility(View.VISIBLE);
            }
        });
    }

    public static void hideSoftKeyboard(Activity activity) {
        InputMethodManager inputMethodManager =
                (InputMethodManager) activity.getSystemService(
                        Activity.INPUT_METHOD_SERVICE);
        inputMethodManager.hideSoftInputFromWindow(
                activity.getCurrentFocus().getWindowToken(), 0);
    }


}