package com.itsanubhav.libdroid.model.settings;

import java.util.List;
import com.google.gson.annotations.SerializedName;

public class BottomNavigation{

	@SerializedName("checked_item_color")
	private String checkedItemColor;

	@SerializedName("visibility")
	private boolean visible;

	@SerializedName("unchecked_item_color")
	private String uncheckedItemColor;

	@SerializedName("background_color")
	private String backgroundColor;

	@SerializedName("show_labels")
	private String showLabels;

	@SerializedName("items")
	private List<ItemsItem> items;

	public void setCheckedItemColor(String checkedItemColor){
		this.checkedItemColor = checkedItemColor;
	}

	public String getCheckedItemColor(){
		return checkedItemColor;
	}

	public void setVisible(boolean visible){
		this.visible = visible;
	}

	public boolean isVisible(){
		return visible;
	}

	public void setUncheckedItemColor(String uncheckedItemColor){
		this.uncheckedItemColor = uncheckedItemColor;
	}

	public String getUncheckedItemColor(){
		return uncheckedItemColor;
	}

	public void setBackgroundColor(String backgroundColor){
		this.backgroundColor = backgroundColor;
	}

	public String getBackgroundColor(){
		return backgroundColor;
	}

	public void setShowLabels(String showLabels){
		this.showLabels = showLabels;
	}

	public String getShowLabels(){
		return showLabels;
	}

	public void setItems(List<ItemsItem> items){
		this.items = items;
	}

	public List<ItemsItem> getItems(){
		return items;
	}
}