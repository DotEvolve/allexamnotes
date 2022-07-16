package com.itsanubhav.libdroid.model.playlist;


import com.google.gson.annotations.SerializedName;

public class Localized{

	@SerializedName("description")
	private String description;

	@SerializedName("title")
	private String title;

	public void setDescription(String description){
		this.description = description;
	}

	public String getDescription(){
		return description;
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
			"Localized{" + 
			"description = '" + description + '\'' + 
			",title = '" + title + '\'' + 
			"}";
		}
}