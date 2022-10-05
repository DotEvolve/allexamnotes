package com.allexamnotes.app;

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

    }

}
