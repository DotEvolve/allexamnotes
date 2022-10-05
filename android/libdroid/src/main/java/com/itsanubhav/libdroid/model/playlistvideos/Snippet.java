package com.itsanubhav.libdroid.model.playlistvideos;

public class Snippet{
	private String playlistId;
	private ResourceId resourceId;
	private String publishedAt;
	private String description;
	private int position;
	private String title;
	private Thumbnails thumbnails;
	private String channelId;
	private String channelTitle;

	public void setPlaylistId(String playlistId){
		this.playlistId = playlistId;
	}

	public String getPlaylistId(){
		return playlistId;
	}

	public void setResourceId(ResourceId resourceId){
		this.resourceId = resourceId;
	}

	public ResourceId getResourceId(){
		return resourceId;
	}

	public void setPublishedAt(String publishedAt){
		this.publishedAt = publishedAt;
	}

	public String getPublishedAt(){
		return publishedAt;
	}

	public void setDescription(String description){
		this.description = description;
	}

	public String getDescription(){
		return description;
	}

	public void setPosition(int position){
		this.position = position;
	}

	public int getPosition(){
		return position;
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
			"playlistId = '" + playlistId + '\'' + 
			",resourceId = '" + resourceId + '\'' + 
			",publishedAt = '" + publishedAt + '\'' + 
			",description = '" + description + '\'' + 
			",position = '" + position + '\'' + 
			",title = '" + title + '\'' + 
			",thumbnails = '" + thumbnails + '\'' + 
			",channelId = '" + channelId + '\'' + 
			",channelTitle = '" + channelTitle + '\'' + 
			"}";
		}
}
