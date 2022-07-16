package com.itsanubhav.libdroid.model;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class WorDroidSection {

    @SerializedName("id")
    private int id;

    @SerializedName("title")
    private String title;

    @SerializedName("items")
    private List<WorDroidSectionItems> items;

    @SerializedName("layout_type")
    private int layoutType;
        //LIST,GRID,SLIDER,CAROUSEL_SLIDER,HORIZONTAL_SECTION

    @SerializedName("category")
    private String category;

    @SerializedName("tags")
    private String tags;

    @SerializedName("content_type")
    private int contentType;
        //POST,PAGE,CATEGORY,TAG


    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getTags() {
        return tags;
    }

    public void setTags(String tags) {
        this.tags = tags;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public List<WorDroidSectionItems> getItems() {
        return items;
    }

    public void setItems(List<WorDroidSectionItems> items) {
        this.items = items;
    }

    public int getLayoutType() {
        return layoutType;
    }

    public void setLayoutType(int layoutType) {
        this.layoutType = layoutType;
    }

    public int getContentType() {
        return contentType;
    }

    public void setContentType(int contentType) {
        this.contentType = contentType;
    }
}
