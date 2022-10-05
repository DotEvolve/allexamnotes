package com.itsanubhav.libdroid.model.media;

import com.google.gson.annotations.SerializedName;

public class Media{

	@SerializedName("date")
	private String date;

	@SerializedName("modified_gmt")
	private String modifiedGmt;

	@SerializedName("author")
	private int author;

	@SerializedName("link")
	private String link;

	@SerializedName("guid")
	private Guid guid;

	@SerializedName("modified")
	private String modified;

	@SerializedName("caption")
	private Caption caption;

	@SerializedName("id")
	private int id;

	@SerializedName("title")
	private Title title;

	@SerializedName("date_gmt")
	private String dateGmt;

	@SerializedName("source_url")
	private String sourceUrl;

	public void setDate(String date){
		this.date = date;
	}

	public String getDate(){
		return date;
	}

	public void setModifiedGmt(String modifiedGmt){
		this.modifiedGmt = modifiedGmt;
	}

	public String getModifiedGmt(){
		return modifiedGmt;
	}

	public void setAuthor(int author){
		this.author = author;
	}

	public int getAuthor(){
		return author;
	}

	public void setLink(String link){
		this.link = link;
	}

	public String getLink(){
		return link;
	}

	public void setGuid(Guid guid){
		this.guid = guid;
	}

	public Guid getGuid(){
		return guid;
	}

	public void setModified(String modified){
		this.modified = modified;
	}

	public String getModified(){
		return modified;
	}

	public void setCaption(Caption caption){
		this.caption = caption;
	}

	public Caption getCaption(){
		return caption;
	}

	public void setId(int id){
		this.id = id;
	}

	public int getId(){
		return id;
	}

	public void setTitle(Title title){
		this.title = title;
	}

	public Title getTitle(){
		return title;
	}

	public void setDateGmt(String dateGmt){
		this.dateGmt = dateGmt;
	}

	public String getDateGmt(){
		return dateGmt;
	}

	public void setSourceUrl(String sourceUrl){
		this.sourceUrl = sourceUrl;
	}

	public String getSourceUrl(){
		return sourceUrl;
	}

	@Override
 	public String toString(){
		return 
			"Media{" + 
			"date = '" + date + '\'' + 
			",modified_gmt = '" + modifiedGmt + '\'' + 
			",author = '" + author + '\'' + 
			",link = '" + link + '\'' + 
			",guid = '" + guid + '\'' + 
			",modified = '" + modified + '\'' + 
			",caption = '" + caption + '\'' + 
			",id = '" + id + '\'' + 
			",title = '" + title + '\'' + 
			",date_gmt = '" + dateGmt + '\'' + 
			",source_url = '" + sourceUrl + '\'' + 
			"}";
		}
}