package com.allexamnotes.libdroid.model.settings;


import com.google.gson.annotations.SerializedName;

public class AppSettings{

	@SerializedName("settings")
	private Settings settings;

	@SerializedName("update")
	private Update update;

	@SerializedName("hidden_cats")
	private String hiddenCats;

	public String getHiddenCats() {
		return hiddenCats;
	}

	public void setHiddenCats(String hiddenCats) {
		this.hiddenCats = hiddenCats;
	}

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