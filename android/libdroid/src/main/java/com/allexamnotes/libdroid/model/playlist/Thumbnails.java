package com.allexamnotes.libdroid.model.playlist;


import com.google.gson.annotations.SerializedName;

public class Thumbnails{

	@SerializedName("standard")
	private Standard standard;

	@SerializedName("default")
	private JsonMemberDefault jsonMemberDefault;

	@SerializedName("high")
	private High high;

	@SerializedName("maxres")
	private Maxres maxres;

	@SerializedName("medium")
	private Medium medium;

	public void setStandard(Standard standard){
		this.standard = standard;
	}

	public Standard getStandard(){
		return standard;
	}

	public void setJsonMemberDefault(JsonMemberDefault jsonMemberDefault){
		this.jsonMemberDefault = jsonMemberDefault;
	}

	public JsonMemberDefault getJsonMemberDefault(){
		return jsonMemberDefault;
	}

	public void setHigh(High high){
		this.high = high;
	}

	public High getHigh(){
		return high;
	}

	public void setMaxres(Maxres maxres){
		this.maxres = maxres;
	}

	public Maxres getMaxres(){
		return maxres;
	}

	public void setMedium(Medium medium){
		this.medium = medium;
	}

	public Medium getMedium(){
		return medium;
	}

	@Override
 	public String toString(){
		return 
			"Thumbnails{" + 
			"standard = '" + standard + '\'' + 
			",default = '" + jsonMemberDefault + '\'' + 
			",high = '" + high + '\'' + 
			",maxres = '" + maxres + '\'' + 
			",medium = '" + medium + '\'' + 
			"}";
		}
}