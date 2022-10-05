package com.allexamnotes.libdroid.model.tag;

import com.google.gson.annotations.SerializedName;

public class Tag{


	@SerializedName("count")
	private int count;

	@SerializedName("link")
	private String link;

	@SerializedName("name")
	private String name;

	@SerializedName("id")
	private int id;

	@SerializedName("slug")
	private String slug;


	public void setCount(int count){
		this.count = count;
	}

	public int getCount(){
		return count;
	}

	public void setLink(String link){
		this.link = link;
	}

	public String getLink(){
		return link;
	}

	public void setName(String name){
		this.name = name;
	}

	public String getName(){
		return name;
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

	@Override
 	public String toString(){
		return 
			"Tag{" +
			",count = '" + count + '\'' + 
			",link = '" + link + '\'' + 
			",name = '" + name + '\'' +
			",id = '" + id + '\'' +
			",slug = '" + slug + '\'' + 
			"}";
		}
}