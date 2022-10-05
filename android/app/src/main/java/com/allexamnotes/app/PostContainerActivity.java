package com.allexamnotes.app;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;

import com.facebook.ads.InterstitialAd;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.FullScreenContentCallback;
import com.google.android.gms.ads.LoadAdError;
import com.google.android.gms.ads.interstitial.InterstitialAdLoadCallback;
import com.allexamnotes.app.fragment.post.PostFragment;

public class PostContainerActivity extends AppCompatActivity {

    private String url;
    private InterstitialAd fbInterstitialAd;
    private com.google.android.gms.ads.interstitial.InterstitialAd adMobInterstitialAd;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        if (Config.FORCE_RTL) {
            getWindow().getDecorView().setLayoutDirection(View.LAYOUT_DIRECTION_RTL);
        }
        super.onCreate(savedInstanceState);
        Window window = getWindow();

        // clear FLAG_TRANSLUCENT_STATUS flag:
        window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);

        // add FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS flag to the window
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);

        // finally change the color
        window.setStatusBarColor(ContextCompat.getColor(getApplicationContext(),R.color.colorPrimary));

        setContentView(R.layout.activity_post_container);
        getIntentData(getIntent());

        /*if (MainApplication.INT_AD_COUNTER>=MainApplication.getAppSettings(getApplicationContext()).getSettings().getPostSettings().getInterstitialAdFrequency()){
            MainApplication.INT_AD_COUNTER = 0;
            fbInterstitialAd.loadAd();
        }*/

        fbInterstitialAd = new InterstitialAd(this, getString(R.string.fb_interstitial_ad_placement_id));
        fbInterstitialAd.loadAd();

        AdRequest adRequest = new AdRequest.Builder().build();
        com.google.android.gms.ads.interstitial.InterstitialAd.load(this,getResources().getString(R.string.admob_interstitial_ad_id), adRequest,
                new InterstitialAdLoadCallback() {
                    @Override
                    public void onAdLoaded(@NonNull com.google.android.gms.ads.interstitial.InterstitialAd interstitialAd) {
                        // The mInterstitialAd reference will be null until
                        // an ad is loaded.
                        adMobInterstitialAd = interstitialAd;
                        adMobInterstitialAd.setFullScreenContentCallback(new FullScreenContentCallback() {
                            @Override
                            public void onAdShowedFullScreenContent() {
                                super.onAdShowedFullScreenContent();
                                adMobInterstitialAd = null;
                            }

                            @Override
                            public void onAdDismissedFullScreenContent() {
                                super.onAdDismissedFullScreenContent();
                                adMobInterstitialAd = null;
                            }
                        });
                    }

                    @Override
                    public void onAdFailedToLoad(@NonNull LoadAdError loadAdError) {
                        // Handle the error
                        adMobInterstitialAd = null;
                    }
                });

        MainApplication.INT_AD_COUNTER++;

    }

    private void getIntentData(Intent intent){
        if (intent!=null){
            String title = intent.getStringExtra("title");
            String img = intent.getStringExtra("img");
            int postId = intent.getIntExtra("postId",0);
            boolean offline = intent.getBooleanExtra("offline",false);
            String slug = intent.getStringExtra("slug");
            url = intent.getStringExtra("url");
            if (postId!=0){
                addFragment(postId,title,img,offline);
            }else if (slug!=null){
                addFragment(slug);
            }else {
                finish();
            }
        }
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        if (intent!=null){
            MainApplication.INT_AD_COUNTER++;
            getIntentData(intent);
        }
    }

    public void addFragment(@NonNull int postId, String title, String imageUrl,boolean offline) {
        PostFragment postFragment = PostFragment.newInstance(postId,title,imageUrl,offline);
        int entryCount = getSupportFragmentManager().getBackStackEntryCount();
        getSupportFragmentManager()
                .beginTransaction()
                .setTransition( FragmentTransaction.TRANSIT_FRAGMENT_OPEN )
                .add(R.id.postContainer, postFragment, "post")
                .addToBackStack("stack"+(entryCount+1))
                .commit();
    }

    public void addFragment(@NonNull String slug) {
        PostFragment postFragment = PostFragment.newInstance(slug);
        int entryCount = getSupportFragmentManager().getBackStackEntryCount();
        getSupportFragmentManager()
                .beginTransaction()
                .setTransition( FragmentTransaction.TRANSIT_FRAGMENT_OPEN )
                .add(R.id.postContainer, postFragment, "post")
                .addToBackStack("stack"+(entryCount+1))
                .commit();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        menu.findItem(R.id.option_share).setVisible(true);
        return super.onCreateOptionsMenu(menu);

    }

    @Override
    public void onBackPressed() {
        if (MainApplication.INT_AD_COUNTER >=MainApplication.getAppSettings(getApplicationContext()).getSettings().getPostSettings().getInterstitialAdFrequency()){
            MainApplication.INT_AD_COUNTER = 0;
            if (adMobInterstitialAd!=null){
                adMobInterstitialAd.show(this);
            }else {
                backButtonPressed();
            }
        }else {
            backButtonPressed();
        }
    }

    public void backButtonPressed(){
        Fragment fragment = getSupportFragmentManager().findFragmentByTag("post");
        int count = getSupportFragmentManager().getBackStackEntryCount();
        if (count <= 1) {
            if (url!=null) {
                startActivity(new Intent(getApplicationContext(), MainActivity.class));
            }
            finish();
        }else {
            if (fragment instanceof PostFragment) {
                getSupportFragmentManager().popBackStackImmediate();
            } else {
                finish();

            }
        }
    }
}
