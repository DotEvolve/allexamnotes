package com.itsanubhav.wordroid4;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.view.Window;
import android.view.WindowManager;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.github.paolorotolo.appintro.AppIntro2;
import com.github.paolorotolo.appintro.AppIntroFragment;
import com.github.paolorotolo.appintro.model.SliderPage;


public class IntroActivity extends AppIntro2 {

        @Override
        protected void onCreate(Bundle savedInstanceState) {
            requestWindowFeature(Window.FEATURE_NO_TITLE);
            getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                    WindowManager.LayoutParams.FLAG_FULLSCREEN);
            super.onCreate(savedInstanceState);
            // Note here that we DO NOT use setContentView();


            // Instead of fragments, you can also use our default slide.
            // Just create a `SliderPage` and provide title, description, background and image.
            // AppIntro will do the rest.
            SliderPage sliderPage1 = new SliderPage();
            sliderPage1.setTitle("REST API V2");
            sliderPage1.setDescription("Works with the latest built in REST API and uses JWT plugin for authentication");
            sliderPage1.setBgColor(Color.parseColor("#0084DA"));
            addSlide(AppIntroFragment.newInstance(sliderPage1));

            SliderPage sliderPage2 = new SliderPage();
            sliderPage2.setTitle("Nested SubCategories");
            sliderPage2.setDescription("Supports unlimited nested sub-categories");
            sliderPage2.setBgColor(Color.parseColor("#4527a0"));
            addSlide(AppIntroFragment.newInstance(sliderPage2));

            SliderPage sliderPage3 = new SliderPage();
            sliderPage3.setTitle("Nested Comments");
            sliderPage3.setDescription("Supports unlimited nested comments");
            sliderPage3.setBgColor(Color.parseColor("#1b5e20"));
            addSlide(AppIntroFragment.newInstance(sliderPage3));


            // Hide Skip/Done button.
            showSkipButton(true);

            // Turn vibration on and set intensity.
            // NOTE: you will probably need to ask VIBRATE permission in Manifest.
            setVibrate(false);
        }

    @Override
    public void onSkipPressed(Fragment currentFragment) {
        super.onSkipPressed(currentFragment);
        introComplete();
    }

    @Override
    public void onDonePressed(Fragment currentFragment) {
        super.onDonePressed(currentFragment);
        introComplete();
    }

    @Override
    public void onSlideChanged(@Nullable Fragment oldFragment, @Nullable Fragment newFragment) {
        super.onSlideChanged(oldFragment, newFragment);
    }

    private void introComplete(){
            SharedPreferences sharedPreferences = getSharedPreferences(Config.defaultSharedPref,MODE_PRIVATE);
            SharedPreferences.Editor editor = sharedPreferences.edit();
            editor.putBoolean("intro_shown",true);
            editor.apply();
            startActivity(new Intent(getApplicationContext(),MainActivity.class));
            finish();
    }

}
