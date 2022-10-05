package com.allexamnotes.libdroid.model.settings;

import java.util.List;
import com.google.gson.annotations.SerializedName;

public class NavDrawer{

	@SerializedName("header_background_color")
	private String headerBackgroundColor;

	@SerializedName("header_visibility")
	private boolean headerVisibility;

	@SerializedName("visibility")
	private boolean visibility;

	@SerializedName("items")
	private List<ItemsItem> items;

	public void setHeaderBackgroundColor(String headerBackgroundColor){
		this.headerBackgroundColor = headerBackgroundColor;
	}

	public String getHeaderBackgroundColor(){
		return headerBackgroundColor;
	}

	public void setHeaderVisibility(boolean headerVisibility){
		this.headerVisibility = headerVisibility;
	}

	public boolean getHeaderVisibility(){
		return headerVisibility;
	}

	public void setVisibility(boolean visibility){
		this.visibility = visibility;
	}

	public boolean isVisibility(){
		return visibility;
	}

	public void setItems(List<ItemsItem> items){
		this.items = items;
	}

	public List<ItemsItem> getItems(){
		return items;
	}
}