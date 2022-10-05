package com.allexamnotes.app.model;

import java.util.List;
import com.google.gson.annotations.SerializedName;


public class MenuDrawer{

	@SerializedName("visible")
	private boolean visible;

	@SerializedName("header_visible")
	private boolean headerVisible;

	@SerializedName("gravity")
	private String gravity;

	@SerializedName("items")
	private List<menuItems> items;

	public void setVisible(boolean visible){
		this.visible = visible;
	}

	public boolean isVisible(){
		return visible;
	}

	public void setHeaderVisible(boolean headerVisible){
		this.headerVisible = headerVisible;
	}

	public boolean isHeaderVisible(){
		return headerVisible;
	}

	public void setGravity(String gravity){
		this.gravity = gravity;
	}

	public String getGravity(){
		return gravity;
	}

	public void setItems(List<menuItems> items){
		this.items = items;
	}

	public List<menuItems> getItems(){
		return items;
	}

	@Override
 	public String toString(){
		return 
			"MenuDrawer{" + 
			"visible = '" + visible + '\'' + 
			",header_visible = '" + headerVisible + '\'' + 
			",gravity = '" + gravity + '\'' + 
			",items = '" + items + '\'' + 
			"}";
		}
}