package com.allexamnotes.libdroid.model.settings;


import com.google.gson.annotations.SerializedName;

public class PostListSettings{

	@SerializedName("native_ads_frequency")
	private int nativeAdsFrequency;

	@SerializedName("item_layout")
	private String itemLayout;

	@SerializedName("banner_ads_enabled")
	private boolean bannerAdsEnabled;

	@SerializedName("native_ads_enabled")
	private boolean nativeAdsEnabled;

	public void setNativeAdsFrequency(int nativeAdsFrequency){
		this.nativeAdsFrequency = nativeAdsFrequency;
	}

	public int getNativeAdsFrequency(){
		return nativeAdsFrequency;
	}

	public void setItemLayout(String itemLayout){
		this.itemLayout = itemLayout;
	}

	public String getItemLayout(){
		return itemLayout;
	}

	public void setBannerAdsEnabled(boolean bannerAdsEnabled){
		this.bannerAdsEnabled = bannerAdsEnabled;
	}

	public boolean isBannerAdsEnabled(){
		return bannerAdsEnabled;
	}

	public void setNativeAdsEnabled(boolean nativeAdsEnabled){
		this.nativeAdsEnabled = nativeAdsEnabled;
	}

	public boolean isNativeAdsEnabled(){
		return nativeAdsEnabled;
	}
}