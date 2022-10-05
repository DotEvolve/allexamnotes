package com.allexamnotes.libdroid.model.playlistvideos;

public class Thumbnails{
	private Standard standard;
	private JsonMemberDefault jsonMemberDefault;
	private High high;
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
			",medium = '" + medium + '\'' + 
			"}";
		}
}
