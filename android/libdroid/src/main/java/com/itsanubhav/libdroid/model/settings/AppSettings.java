package com.itsanubhav.libdroid.model.settings;


import com.google.gson.annotations.SerializedName;

public class AppSettings{

	@SerializedName("settings")
	private Settings settings;

	@SerializedName("update")
	private Update update;

	public void setSettings(Settings settings){
		this.settings = settings;
	}

	public Settings getSettings(){
		return settings;
	}

	public void setUpdate(Update update){
		this.update = update;
	}

	public Update getUpdate(){
		return update;
	}
}