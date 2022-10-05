package com.itsanubhav.libdroid.model.playlistvideos;

public class ResourceId{
	private String kind;
	private String videoId;

	public void setKind(String kind){
		this.kind = kind;
	}

	public String getKind(){
		return kind;
	}

	public void setVideoId(String videoId){
		this.videoId = videoId;
	}

	public String getVideoId(){
		return videoId;
	}

	@Override
 	public String toString(){
		return 
			"ResourceId{" + 
			"kind = '" + kind + '\'' + 
			",videoId = '" + videoId + '\'' + 
			"}";
		}
}
