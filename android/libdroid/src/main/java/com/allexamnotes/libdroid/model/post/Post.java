package com.allexamnotes.libdroid.model.post;

import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;
import androidx.room.TypeConverters;

import com.google.gson.annotations.SerializedName;
import com.allexamnotes.libdroid.database.convertors.ContentConvertor;
import com.allexamnotes.libdroid.database.convertors.TitleConvertor;

import java.util.List;

@Entity
public class Post{

	@SerializedName("date")
	public String date;

	@SerializedName("wordroid4")
	public String template;

	@SerializedName("author_name")
	public String authorName;

	@SerializedName("comment_count")
	public int commentCount;

	@SerializedName("content")
	@TypeConverters(ContentConvertor.class)
	public Content content;

	@SerializedName("author")
	public int author;

	@SerializedName("link")
	public String link;

	@SerializedName("format")
	public String format;

	@SerializedName("type")
	public String type;

	@SerializedName("title")
	@TypeConverters(TitleConvertor.class)
	public Title title;

	@SerializedName("featured_img")
	public String featuredImgUrl;

	@SerializedName("categories_details")
	@Ignore
	public List<CategoriesDetailItem> categoriesDetails;

	@SerializedName("categories")
	@Ignore
	public List<Integer> categories;

	@SerializedName("comment_status")
	public String commentStatus;

	@SerializedName("featured_media")
	public int featuredMedia;

	@SerializedName("ping_status")
	public String pingStatus;

	@SerializedName("sticky")
	public boolean sticky;

	@SerializedName("modified")
	public String modified;

	@SerializedName("id")
	@PrimaryKey
	public int id;

	@SerializedName("slug")
	public String slug;

	@SerializedName("status")
	public String status;

	public Content getContent() {
		return content;
	}

	public void setContent(Content content) {
		this.content = content;
	}

	public String getFeaturedImgUrl() {
		return featuredImgUrl;
	}

	public void setFeaturedImgUrl(String featuredImgUrl) {
		this.featuredImgUrl = featuredImgUrl;
	}

	public void setDate(String date){
		this.date = date;
	}

	public String getDate(){
		return date;
	}

	public void setTemplate(String template){
		this.template = template;
	}

	public String getTemplate(){
		return template;
	}

	public void setAuthorName(String authorName){
		this.authorName = authorName;
	}

	public String getAuthorName(){
		return authorName;
	}

	public void setCommentCount(int commentCount){
		this.commentCount = commentCount;
	}

	public int getCommentCount(){
		return commentCount;
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

	public void setFormat(String format){
		this.format = format;
	}

	public String getFormat(){
		return format;
	}

	public void setType(String type){
		this.type = type;
	}

	public String getType(){
		return type;
	}

	public void setTitle(Title title){
		this.title = title;
	}

	public Title getTitle(){
		return title;
	}

	public List<Integer> getCategories() {
		return categories;
	}

	public void setCategories(List<Integer> categories) {
		this.categories = categories;
	}

	public List<CategoriesDetailItem> getCategoriesDetails() {
		return categoriesDetails;
	}

	public void setCategoriesDetails(List<CategoriesDetailItem> categoriesDetails) {
		this.categoriesDetails = categoriesDetails;
	}


	public void setCommentStatus(String commentStatus){
		this.commentStatus = commentStatus;
	}

	public String getCommentStatus(){
		return commentStatus;
	}


	public void setFeaturedMedia(int featuredMedia){
		this.featuredMedia = featuredMedia;
	}

	public int getFeaturedMedia(){
		return featuredMedia;
	}

	public void setPingStatus(String pingStatus){
		this.pingStatus = pingStatus;
	}

	public String getPingStatus(){
		return pingStatus;
	}

	public void setSticky(boolean sticky){
		this.sticky = sticky;
	}

	public boolean isSticky(){
		return sticky;
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

	public void setSlug(String slug){
		this.slug = slug;
	}

	public String getSlug(){
		return slug;
	}

	public void setStatus(String status){
		this.status = status;
	}

	public String getStatus(){
		return status;
	}

	@Override
	public boolean equals(Object o) {
		try {
			Post p = (Post) o;
			return this.id == p.id;
		}catch (ClassCastException e){
			return false;
		}
	}

	@Override
 	public String toString(){
		return 
			"Post{" + 
			"date = '" + date + '\'' + 
			",wordroid4 = '" + template + '\'' +
			",author_name = '" + authorName + '\'' + 
			",comment_count = '" + commentCount + '\'' + 
			",author = '" + author + '\'' + 
			",link = '" + link + '\'' + 
			",format = '" + format + '\'' + 
			",type = '" + type + '\'' + 
			",title = '" + title + '\'' +
			",comment_status = '" + commentStatus + '\'' +
			",featured_media = '" + featuredMedia + '\'' + 
			",ping_status = '" + pingStatus + '\'' + 
			",sticky = '" + sticky + '\'' + 
			",modified = '" + modified + '\'' +
			",id = '" + id + '\'' + 
			",slug = '" + slug + '\'' + 
			",status = '" + status + '\'' + 
			"}";
		}
}