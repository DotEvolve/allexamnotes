package com.allexamnotes.libdroid.model.comment;

import java.util.List;
import com.google.gson.annotations.SerializedName;

public class Comment{

	@SerializedName("author_name")
	private String authorName;

	@SerializedName("date")
	private String date;

	@SerializedName("parent")
	private int parent;

	@SerializedName("child_count")
	private int childCommentCount;

	@SerializedName("author")
	private int author;

	@SerializedName("link")
	private String link;

	@SerializedName("type")
	private String type;

	@SerializedName("content")
	private Content content;

	@SerializedName("author_url")
	private String authorUrl;

	@SerializedName("post")
	private int post;

	@SerializedName("author_avatar_urls")
	private AuthorAvatarUrls authorAvatarUrls;

	@SerializedName("meta")
	private List<Object> meta;

	@SerializedName("id")
	private int id;

	@SerializedName("date_gmt")
	private String dateGmt;

	@SerializedName("status")
	private String status;

	public void setAuthorName(String authorName){
		this.authorName = authorName;
	}

	public String getAuthorName(){
		return authorName;
	}

	public void setDate(String date){
		this.date = date;
	}

	public String getDate(){
		return date;
	}

	public void setParent(int parent){
		this.parent = parent;
	}

	public int getParent(){
		return parent;
	}

	public void setChildCommentCount(int childCommentCount){
		this.childCommentCount = childCommentCount;
	}

	public int getChildCommentCount(){
		return childCommentCount;
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

	public void setType(String type){
		this.type = type;
	}

	public String getType(){
		return type;
	}

	public void setContent(Content content){
		this.content = content;
	}

	public Content getContent(){
		return content;
	}

	public void setAuthorUrl(String authorUrl){
		this.authorUrl = authorUrl;
	}

	public String getAuthorUrl(){
		return authorUrl;
	}

	public void setPost(int post){
		this.post = post;
	}

	public int getPost(){
		return post;
	}

	public void setAuthorAvatarUrls(AuthorAvatarUrls authorAvatarUrls){
		this.authorAvatarUrls = authorAvatarUrls;
	}

	public AuthorAvatarUrls getAuthorAvatarUrls(){
		return authorAvatarUrls;
	}

	public void setMeta(List<Object> meta){
		this.meta = meta;
	}

	public List<Object> getMeta(){
		return meta;
	}

	public void setId(int id){
		this.id = id;
	}

	public int getId(){
		return id;
	}

	public void setDateGmt(String dateGmt){
		this.dateGmt = dateGmt;
	}

	public String getDateGmt(){
		return dateGmt;
	}

	public void setStatus(String status){
		this.status = status;
	}

	public String getStatus(){
		return status;
	}

	@Override
 	public String toString(){
		return 
			"Comment{" + 
			"author_name = '" + authorName + '\'' + 
			",date = '" + date + '\'' + 
			",parent = '" + parent + '\'' + 
			",child_comment_count = '" + childCommentCount + '\'' + 
			",author = '" + author + '\'' + 
			",link = '" + link + '\'' + 
			",type = '" + type + '\'' + 
			",content = '" + content + '\'' + 
			",author_url = '" + authorUrl + '\'' + 
			",post = '" + post + '\'' + 
			",author_avatar_urls = '" + authorAvatarUrls + '\'' + 
			",meta = '" + meta + '\'' + 
			",id = '" + id + '\'' + 
			",date_gmt = '" + dateGmt + '\'' + 
			",status = '" + status + '\'' + 
			"}";
		}
}