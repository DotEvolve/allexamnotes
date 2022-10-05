package com.itsanubhav.libdroid.model.auth;

import com.google.gson.annotations.SerializedName;

public class AuthResponse{

	@SerializedName("user_email")
	private String userEmail;

	@SerializedName("user_nicename")
	private String userNicename;

	@SerializedName("user_display_name")
	private String userDisplayName;

	@SerializedName("token")
	private String token;

	@SerializedName("code")
	private String code;

	@SerializedName("message")
	private String message;

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public void setUserEmail(String userEmail){
		this.userEmail = userEmail;
	}

	public String getUserEmail(){
		return userEmail;
	}

	public void setUserNicename(String userNicename){
		this.userNicename = userNicename;
	}

	public String getUserNicename(){
		return userNicename;
	}

	public void setUserDisplayName(String userDisplayName){
		this.userDisplayName = userDisplayName;
	}

	public String getUserDisplayName(){
		return userDisplayName;
	}

	public void setToken(String token){
		this.token = token;
	}

	public String getToken(){
		return token;
	}

	@Override
 	public String toString(){
		return 
			"AuthResponse{" + 
			"user_email = '" + userEmail + '\'' + 
			",user_nicename = '" + userNicename + '\'' + 
			",user_display_name = '" + userDisplayName + '\'' + 
			",token = '" + token + '\'' + 
			"}";
		}
}