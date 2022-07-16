package com.itsanubhav.wordroid4.model;


import com.google.gson.annotations.SerializedName;

public class ComplexHome{

	@SerializedName("menu_drawer")
	private MenuDrawer menuDrawer;

	@SerializedName("bottom_navigation_menu")
	private BottomNavigationMenu bottomNavigationMenu;

	public void setMenuDrawer(MenuDrawer menuDrawer){
		this.menuDrawer = menuDrawer;
	}

	public MenuDrawer getMenuDrawer(){
		return menuDrawer;
	}

	public void setBottomNavigationMenu(BottomNavigationMenu bottomNavigationMenu){
		this.bottomNavigationMenu = bottomNavigationMenu;
	}

	public BottomNavigationMenu getBottomNavigationMenu(){
		return bottomNavigationMenu;
	}

	@Override
 	public String toString(){
		return 
			"ComplexHome{" + 
			"menu_drawer = '" + menuDrawer + '\'' + 
			",bottom_navigation_menu = '" + bottomNavigationMenu + '\'' + 
			"}";
		}
}