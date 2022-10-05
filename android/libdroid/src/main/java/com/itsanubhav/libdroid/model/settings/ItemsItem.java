package com.itsanubhav.libdroid.model.settings;

import com.google.gson.annotations.SerializedName;

public class ItemsItem{

	@SerializedName("icon_color")
	private String iconColor;

	@SerializedName("data")
	private String data;

	@SerializedName("icon")
	private String icon;

	@SerializedName("destination")
	private int destination;

	@SerializedName("title")
	private String title;

	public void setIconColor(String iconColor){
		this.iconColor = iconColor;
	}

	public String getIconColor(){
		return iconColor;
	}

	public void setData(String data){
		this.data = data;
	}

	public String getData(){
		return data;
	}

	public void setIcon(String icon){
		this.icon = icon;
	}

	public String getIcon(){
		return icon;
	}

	public void setDestination(int destination){
		this.destination = destination;
	}

	public int getDestination(){
		return destination;
	}

	public void setTitle(String title){
		this.title = title;
	}

	public String getTitle(){
		return title;
	}
}