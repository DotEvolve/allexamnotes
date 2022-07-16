package com.itsanubhav.libdroid.model.settings;

import com.google.gson.annotations.SerializedName;

public class Update{

	@SerializedName("msg")
	private String msg;

	@SerializedName("title")
	private String title;

	@SerializedName("version")
	private int version;

	@SerializedName("force_update")
	private boolean forceUpdate;

	public void setMsg(String msg){
		this.msg = msg;
	}

	public String getMsg(){
		return msg;
	}

	public void setTitle(String title){
		this.title = title;
	}

	public String getTitle(){
		return title;
	}

	public void setVersion(int version){
		this.version = version;
	}

	public int getVersion(){
		return version;
	}

	public void setForceUpdate(boolean forceUpdate){
		this.forceUpdate = forceUpdate;
	}

	public boolean isForceUpdate(){
		return forceUpdate;
	}
}