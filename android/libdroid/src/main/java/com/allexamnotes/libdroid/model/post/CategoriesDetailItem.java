package com.allexamnotes.libdroid.model.post;


import com.google.gson.annotations.SerializedName;


public class CategoriesDetailItem{

	@SerializedName("parent")
	private int parent;

	@SerializedName("name")
	private String name;

	@SerializedName("count")
	private int count;

	@SerializedName("id")
	private int id;


	public void setParent(int parent){
		this.parent = parent;
	}

	public int getParent(){
		return parent;
	}

	public void setName(String name){
		this.name = name;
	}

	public String getName(){
		return name;
	}

	public void setCount(int count){
		this.count = count;
	}

	public int getCount(){
		return count;
	}


	public void setId(int id){
		this.id = id;
	}

	public int getId(){
		return id;
	}

	@Override
 	public String toString(){
		return 
			"CategoriesDetailItem{" + 
			"parent = '" + parent + '\'' + 
			",name = '" + name + '\'' + 
			",count = '" + count + '\'' +
			",id = '" + id + '\'' +
			"}";
		}
}