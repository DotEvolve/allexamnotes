package com.itsanubhav.libdroid.model;

import com.google.gson.annotations.SerializedName;

public class WorDroidSectionItems {

    @SerializedName("id")
    private int id;

    @SerializedName("modified")
    private String modified;

    @SerializedName("post_views")
    private int postViews;

    @SerializedName("title")
    private String title;

    @SerializedName("author_name")
    private String authorName;

    @SerializedName("featured_img")
    private String featuredImg;

    @SerializedName("comment_count")
    private int commentCount;

    @SerializedName("count")
    private int count;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getModified() {
        return modified;
    }

    public void setModified(String modified) {
        this.modified = modified;
    }

    public int getPostViews() {
        return postViews;
    }

    public void setPostViews(int postViews) {
        this.postViews = postViews;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getAuthorName() {
        return authorName;
    }

    public void setAuthorName(String authorName) {
        this.authorName = authorName;
    }

    public String getFeaturedImg() {
        return featuredImg;
    }

    public void setFeaturedImg(String featuredImg) {
        this.featuredImg = featuredImg;
    }

    public int getCommentCount() {
        return commentCount;
    }

    public void setCommentCount(int commentCount) {
        this.commentCount = commentCount;
    }

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }

    @Override
    public String toString() {
        return "WorDroidSectionItems{" +
                "id=" + id +
                ", modified='" + modified + '\'' +
                ", postViews=" + postViews +
                ", title='" + title + '\'' +
                ", authorName='" + authorName + '\'' +
                ", featuredImg='" + featuredImg + '\'' +
                ", commentCount=" + commentCount +
                ", count=" + count +
                '}';
    }
}
