package com.itsanubhav.wordroid4.others;

import com.google.gson.annotations.SerializedName;


public class Settings{

	@SerializedName("slider_on_post_list")
	private boolean sliderOnPostList;

	@SerializedName("open_external_links_in_app")
	private boolean openExternalLinksInApp;

	@SerializedName("app_url")
	private String appUrl;

	@SerializedName("app_title")
	private String appTitle;

	@SerializedName("signin_enabled")
	private boolean signinEnabled;

	/*@SerializedName("menu_drawer_items")
	private List<MenuDrawerItemsItem> menuDrawerItems;*/

	@SerializedName("primary_color")
	private String primaryColor;

	@SerializedName("default_image_url")
	private String defaultImageUrl;

	@SerializedName("rtl_enabled")
	private boolean rtlEnabled;

	@SerializedName("first_big_item_on_post_list")
	private boolean firstBigItemOnPostList;

	@SerializedName("secondary_color")
	private String secondaryColor;

	/*@SerializedName("banner_ads")
	private BannerAds bannerAds;*/

	@SerializedName("app_intro_enabled")
	private boolean appIntroEnabled;

	@SerializedName("homepage")
	private int homepage;

	public void setSliderOnPostList(boolean sliderOnPostList){
		this.sliderOnPostList = sliderOnPostList;
	}

	public boolean isSliderOnPostList(){
		return sliderOnPostList;
	}

	public void setOpenExternalLinksInApp(boolean openExternalLinksInApp){
		this.openExternalLinksInApp = openExternalLinksInApp;
	}

	public boolean isOpenExternalLinksInApp(){
		return openExternalLinksInApp;
	}

	public void setAppUrl(String appUrl){
		this.appUrl = appUrl;
	}

	public String getAppUrl(){
		return appUrl;
	}

	public void setAppTitle(String appTitle){
		this.appTitle = appTitle;
	}

	public String getAppTitle(){
		return appTitle;
	}

	public void setSigninEnabled(boolean signinEnabled){
		this.signinEnabled = signinEnabled;
	}

	public boolean isSigninEnabled(){
		return signinEnabled;
	}

	/*public void setMenuDrawerItems(List<MenuDrawerItemsItem> menuDrawerItems){
		this.menuDrawerItems = menuDrawerItems;
	}

	public List<MenuDrawerItemsItem> getMenuDrawerItems(){
		return menuDrawerItems;
	}*/

	public void setPrimaryColor(String primaryColor){
		this.primaryColor = primaryColor;
	}

	public String getPrimaryColor(){
		return primaryColor;
	}

	public void setDefaultImageUrl(String defaultImageUrl){
		this.defaultImageUrl = defaultImageUrl;
	}

	public String getDefaultImageUrl(){
		return defaultImageUrl;
	}

	public void setRtlEnabled(boolean rtlEnabled){
		this.rtlEnabled = rtlEnabled;
	}

	public boolean isRtlEnabled(){
		return rtlEnabled;
	}

	public void setFirstBigItemOnPostList(boolean firstBigItemOnPostList){
		this.firstBigItemOnPostList = firstBigItemOnPostList;
	}

	public boolean isFirstBigItemOnPostList(){
		return firstBigItemOnPostList;
	}

	public void setSecondaryColor(String secondaryColor){
		this.secondaryColor = secondaryColor;
	}

	public String getSecondaryColor(){
		return secondaryColor;
	}

	/*public void setBannerAds(BannerAds bannerAds){
		this.bannerAds = bannerAds;
	}

	public BannerAds getBannerAds(){
		return bannerAds;
	}*/

	public void setAppIntroEnabled(boolean appIntroEnabled){
		this.appIntroEnabled = appIntroEnabled;
	}

	public boolean isAppIntroEnabled(){
		return appIntroEnabled;
	}

	public void setHomepage(int homepage){
		this.homepage = homepage;
	}

	public int getHomepage(){
		return homepage;
	}
}