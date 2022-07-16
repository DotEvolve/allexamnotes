package com.itsanubhav.wordroid4.model;

import com.google.gson.annotations.SerializedName;


public class Settings{

	@SerializedName("complex_home")
	private ComplexHome complexHome;

	public void setComplexHome(ComplexHome complexHome){
		this.complexHome = complexHome;
	}

	public ComplexHome getComplexHome(){
		return complexHome;
	}

	@Override
 	public String toString(){
		return 
			"Settings{" + 
			"complex_home = '" + complexHome + '\'' + 
			"}";
		}
}