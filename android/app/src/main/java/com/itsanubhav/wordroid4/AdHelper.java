package com.itsanubhav.wordroid4;

import android.content.Context;


import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdLoader;
import com.google.android.gms.ads.formats.NativeAdOptions;
import com.google.android.gms.ads.formats.UnifiedNativeAd;


interface AdCallback{
    void onAdMobAd(UnifiedNativeAd unifiedNativeAd);
}


public class AdHelper{

    private static String UNIT_ID = "ca-app-pub-3940256099942544/2247696110";

    private AdCallback mListener;

    public void setAdListener(AdCallback mListener) {
        this.mListener = mListener;
    }

    public static void adMobAds(Context context){
        AdLoader adLoader = new AdLoader.Builder(context, UNIT_ID)
                .forUnifiedNativeAd(new UnifiedNativeAd.OnUnifiedNativeAdLoadedListener() {
                    @Override
                    public void onUnifiedNativeAdLoaded(UnifiedNativeAd unifiedNativeAd) {
                        // Show the ad.

                    }
                })
                .withAdListener(new AdListener() {
                    @Override
                    public void onAdFailedToLoad(int errorCode) {
                        // Handle the failure by logging, altering the UI, and so on.
                    }
                })
                .withNativeAdOptions(new NativeAdOptions.Builder()
                        // Methods in the NativeAdOptions.Builder class can be
                        // used here to specify individual options settings.
                        .build())
                .build();
    }

}
