package com.itsanubhav.wordroid4.model;

import java.util.List;
import com.google.gson.annotations.SerializedName;

public class BottomNavigationMenu{

	@SerializedName("elevation")
	private int elevation;

	@SerializedName("visible")
	private boolean visible;

	@SerializedName("background_color")
	private String backgroundColor;

	@SerializedName("checked_item_color")
	private String checkedItemColor;

	@SerializedName("unchecked_item_color")
	private String uncheckedItemColor;

	@SerializedName("show_labels")
	private String showLabels;

	@SerializedName("items")
	private List<menuItems> items;

	public void setElevation(int elevation){
		this.elevation = elevation;
	}

	public int getElevation(){
		return elevation;
	}

	public void setVisible(boolean visible){
		this.visible = visible;
	}

	public boolean isVisible(){
		return visible;
	}

	public void setBackgroundColor(String backgroundColor){
		this.backgroundColor = backgroundColor;
	}

	public String getBackgroundColor(){
		return backgroundColor;
	}

	public String getCheckedItemColor() {
		return checkedItemColor;
	}

	public void setCheckedItemColor(String checkedItemColor) {
		this.checkedItemColor = checkedItemColor;
	}

	public String getUncheckedItemColor() {
		return uncheckedItemColor;
	}

	public void setUncheckedItemColor(String uncheckedItemColor) {
		this.uncheckedItemColor = uncheckedItemColor;
	}

	public void setShowLabels(String showLabels){
		this.showLabels = showLabels;
	}

	public String getShowLabels(){
		return showLabels;
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
			"BottomNavigationMenu{" + 
			"elevation = '" + elevation + '\'' + 
			",visible = '" + visible + '\'' + 
			",background_color = '" + backgroundColor + '\'' + 
			",show_labels = '" + showLabels + '\'' + 
			",items = '" + items + '\'' + 
			"}";
		}
}