package com.itsanubhav.libdroid.model.playlistvideos;

public class ContentDetails{
	private String videoPublishedAt;
	private String videoId;

	public void setVideoPublishedAt(String videoPublishedAt){
		this.videoPublishedAt = videoPublishedAt;
	}

	public String getVideoPublishedAt(){
		return videoPublishedAt;
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
			"ContentDetails{" + 
			"videoPublishedAt = '" + videoPublishedAt + '\'' + 
			",videoId = '" + videoId + '\'' + 
			"}";
		}
}
