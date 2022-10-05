package com.allexamnotes.libdroid.model.playlist;


import com.google.gson.annotations.SerializedName;

public class Snippet{

	@SerializedName("publishedAt")
	private String publishedAt;

	@SerializedName("localized")
	private Localized localized;

	@SerializedName("description")
	private String description;

	@SerializedName("title")
	private String title;

	@SerializedName("thumbnails")
	private Thumbnails thumbnails;

	@SerializedName("channelId")
	private String channelId;

	@SerializedName("channelTitle")
	private String channelTitle;

	public void setPublishedAt(String publishedAt){
		this.publishedAt = publishedAt;
	}

	public String getPublishedAt(){
		return publishedAt;
	}

	public void setLocalized(Localized localized){
		this.localized = localized;
	}

	public Localized getLocalized(){
		return localized;
	}

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

	public void setThumbnails(Thumbnails thumbnails){
		this.thumbnails = thumbnails;
	}

	public Thumbnails getThumbnails(){
		return thumbnails;
	}

	public void setChannelId(String channelId){
		this.channelId = channelId;
	}

	public String getChannelId(){
		return channelId;
	}

	public void setChannelTitle(String channelTitle){
		this.channelTitle = channelTitle;
	}

	public String getChannelTitle(){
		return channelTitle;
	}

	@Override
 	public String toString(){
		return 
			"Snippet{" + 
			"publishedAt = '" + publishedAt + '\'' + 
			",localized = '" + localized + '\'' + 
			",description = '" + description + '\'' + 
			",title = '" + title + '\'' + 
			",thumbnails = '" + thumbnails + '\'' + 
			",channelId = '" + channelId + '\'' + 
			",channelTitle = '" + channelTitle + '\'' + 
			"}";
		}
}