package com.itsanubhav.libdroid.model.settings;

import com.google.gson.annotations.SerializedName;

public class PostSettings{

	@SerializedName("interstitial_ads_enabled")
	private boolean interstitialAdsEnabled;

	@SerializedName("external_links")
	private String externalLinks;

	@SerializedName("speak_enabled")
	private boolean speakEnabled;

	@SerializedName("banner_ads_enabled")
	private boolean bannerAdsEnabled;

	@SerializedName("content_copy_enabled")
	private boolean contentCopyEnabled;

	@SerializedName("interstitial_ad_frequency")
	private int interstitialAdFrequency;

	public void setInterstitialAdsEnabled(boolean interstitialAdsEnabled){
		this.interstitialAdsEnabled = interstitialAdsEnabled;
	}

	public boolean isInterstitialAdsEnabled(){
		return interstitialAdsEnabled;
	}

	public void setExternalLinks(String externalLinks){
		this.externalLinks = externalLinks;
	}

	public String getExternalLinks(){
		return externalLinks;
	}

	public void setSpeakEnabled(boolean speakEnabled){
		this.speakEnabled = speakEnabled;
	}

	public boolean isSpeakEnabled(){
		return speakEnabled;
	}

	public void setBannerAdsEnabled(boolean bannerAdsEnabled){
		this.bannerAdsEnabled = bannerAdsEnabled;
	}

	public boolean isBannerAdsEnabled(){
		return bannerAdsEnabled;
	}

	public void setContentCopyEnabled(boolean contentCopyEnabled){
		this.contentCopyEnabled = contentCopyEnabled;
	}

	public boolean isContentCopyEnabled(){
		return contentCopyEnabled;
	}

	public void setInterstitialAdFrequency(int interstitialAdFrequency){
		this.interstitialAdFrequency = interstitialAdFrequency;
	}

	public int getInterstitialAdFrequency(){
		return interstitialAdFrequency;
	}
}