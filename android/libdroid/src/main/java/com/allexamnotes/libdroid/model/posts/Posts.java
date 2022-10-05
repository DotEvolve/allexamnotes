package com.allexamnotes.libdroid.model.posts;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

import com.google.gson.annotations.SerializedName;

@Entity
public class Posts{

	@SerializedName("comment_count")
	@ColumnInfo(name = "comment_count")
	public int commentCount;

	@SerializedName("author_name")
	@ColumnInfo(name = "author_name")
	public String authorName;

	@SerializedName("author")
	@ColumnInfo(name = "author_id")
	public int author;

	@SerializedName("modified")
	@ColumnInfo(name = "modified")
	public String modified;

	@SerializedName("id")
	@PrimaryKey
	public int id;

	@SerializedName("post_views")
	@ColumnInfo(name = "post_views")
	public int postViews;

	@SerializedName("title")
	@ColumnInfo(name = "title")
	public String title;

	@SerializedName("comment_status")
	@ColumnInfo(name = "comment_status")
	public boolean commentStatus;

	@SerializedName("featured_img")
	@ColumnInfo(name = "featured_img")
	public String featuredImg;

	public void setCommentCount(int commentCount){
		this.commentCount = commentCount;
	}

	public int getCommentCount(){
		return commentCount;
	}

	public void setAuthorName(String authorName){
		this.authorName = authorName;
	}

	public String getAuthorName(){
		return authorName;
	}

	public void setAuthor(int author){
		this.author = author;
	}

	public int getAuthor(){
		return author;
	}

	public void setModified(String modified){
		this.modified = modified;
	}

	public String getModified(){
		return modified;
	}

	public void setId(int id){
		this.id = id;
	}

	public int getId(){
		return id;
	}

	public void setPostViews(int postViews){
		this.postViews = postViews;
	}

	public int getPostViews(){
		return postViews;
	}

	public void setTitle(String title){
		this.title = title;
	}

	public String getTitle(){
		return title;
	}

	public void setCommentStatus(boolean commentStatus){
		this.commentStatus = commentStatus;
	}

	public boolean isCommentStatus(){
		return commentStatus;
	}

	public void setFeaturedImg(String featuredImg){
		this.featuredImg = featuredImg;
	}

	public String getFeaturedImg(){
		return featuredImg;
	}

	@Override
 	public String toString(){
		return 
			"Posts{" + 
			"comment_count = '" + commentCount + '\'' + 
			",author_name = '" + authorName + '\'' + 
			",author = '" + author + '\'' + 
			",modified = '" + modified + '\'' + 
			",id = '" + id + '\'' + 
			",post_views = '" + postViews + '\'' + 
			",title = '" + title + '\'' + 
			",comment_status = '" + commentStatus + '\'' + 
			",featured_img = '" + featuredImg + '\'' + 
			"}";
		}
}