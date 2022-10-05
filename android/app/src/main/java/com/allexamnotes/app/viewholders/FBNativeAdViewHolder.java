package com.allexamnotes.app.viewholders;

import android.view.View;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.facebook.ads.NativeAdLayout;
import com.allexamnotes.app.R;

public class FBNativeAdViewHolder extends RecyclerView.ViewHolder{

    NativeAdLayout fbNativeAd;

    public NativeAdLayout getAdView() {
        return fbNativeAd;
    }

    public FBNativeAdViewHolder(@NonNull View itemView) {
        super(itemView);

        fbNativeAd = itemView.findViewById(R.id.fbNativeAd);

    }
}
