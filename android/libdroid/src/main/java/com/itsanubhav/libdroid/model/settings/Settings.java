package com.itsanubhav.libdroid.model.settings;

import com.google.gson.annotations.SerializedName;

public class Settings{

	@SerializedName("app_name")
	private String appName;

	@SerializedName("bottom_navigation")
	private BottomNavigation bottomNavigation;

	@SerializedName("about_url")
	private String aboutUrl;

	@SerializedName("signin")
	private boolean signin;

	@SerializedName("post_settings")
	private PostSettings postSettings;

	@SerializedName("privacy_url")
	private String privacyUrl;

	@SerializedName("secondary_color")
	private String secondaryColor;

	@SerializedName("primary_color")
	private String primaryColor;

	@SerializedName("default_img")
	private String defaultImg;

	@SerializedName("post_list_settings")
	private PostListSettings postListSettings;

	@SerializedName("app_intro")
	private boolean appIntro;

	@SerializedName("nav_drawer")
	private NavDrawer navDrawer;

	public void setAppName(String appName){
		this.appName = appName;
	}

	public String getAppName(){
		return appName;
	}

	public void setBottomNavigation(BottomNavigation bottomNavigation){
		this.bottomNavigation = bottomNavigation;
	}

	public BottomNavigation getBottomNavigation(){
		return bottomNavigation;
	}

	public void setAboutUrl(String aboutUrl){
		this.aboutUrl = aboutUrl;
	}

	public String getAboutUrl(){
		return aboutUrl;
	}

	public void setSignin(boolean signin){
		this.signin = signin;
	}

	public boolean isSignin(){
		return signin;
	}

	public void setPostSettings(PostSettings postSettings){
		this.postSettings = postSettings;
	}

	public PostSettings getPostSettings(){
		return postSettings;
	}

	public void setPrivacyUrl(String privacyUrl){
		this.privacyUrl = privacyUrl;
	}

	public String getPrivacyUrl(){
		return privacyUrl;
	}

	public void setSecondaryColor(String secondaryColor){
		this.secondaryColor = secondaryColor;
	}

	public String getSecondaryColor(){
		return secondaryColor;
	}

	public void setPrimaryColor(String primaryColor){
		this.primaryColor = primaryColor;
	}

	public String getPrimaryColor(){
		return primaryColor;
	}

	public void setDefaultImg(String defaultImg){
		this.defaultImg = defaultImg;
	}

	public String getDefaultImg(){
		return defaultImg;
	}

	public void setPostListSettings(PostListSettings postListSettings){
		this.postListSettings = postListSettings;
	}

	public PostListSettings getPostListSettings(){
		return postListSettings;
	}

	public void setAppIntro(boolean appIntro){
		this.appIntro = appIntro;
	}

	public boolean isAppIntro(){
		return appIntro;
	}

	public void setNavDrawer(NavDrawer navDrawer){
		this.navDrawer = navDrawer;
	}

	public NavDrawer getNavDrawer(){
		return navDrawer;
	}
}