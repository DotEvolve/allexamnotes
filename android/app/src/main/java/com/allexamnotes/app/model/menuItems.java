package com.allexamnotes.app.model;


import com.google.gson.annotations.SerializedName;


public class menuItems {

	@SerializedName("data")
	private Object data;

	@SerializedName("icon")
	private String icon;

	@SerializedName("destination")
	private int destination;

	@SerializedName("title")
	private String title;

	@SerializedName("icon-color")
	private String iconColor;

	public String getIconColor() {
		return iconColor;
	}

	public void setIconColor(String iconColor) {
		this.iconColor = iconColor;
	}

	public void setData(Object data){
		this.data = data;
	}

	public Object getData(){
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

	@Override
 	public String toString(){
		return 
			"menuItems{" +
			"data = '" + data + '\'' + 
			",icon = '" + icon + '\'' + 
			",destination = '" + destination + '\'' + 
			",title = '" + title + '\'' + 
			"}";
		}
}