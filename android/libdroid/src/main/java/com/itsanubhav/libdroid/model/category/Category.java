package com.itsanubhav.libdroid.model.category;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

import com.google.gson.annotations.SerializedName;

@Entity
public class Category{

	@SerializedName("parent")
	@ColumnInfo(name = "parent")
	public int parent;

	@SerializedName("count")
	@ColumnInfo(name = "count")
	public int count;

	@SerializedName("name")
	@ColumnInfo(name = "name")
	public String name;

	@SerializedName("id")
	@PrimaryKey
	public int id;

	@SerializedName("hidden")
	@ColumnInfo(name = "hidden")
	private boolean hidden;

	public boolean isHidden() {
		return hidden;
	}

	public void setHidden(boolean hidden) {
		this.hidden = hidden;
	}

	public void setParent(int parent){
		this.parent = parent;
	}

	public int getParent(){
		return parent;
	}


	public void setCount(int count){
		this.count = count;
	}

	public int getCount(){
		return count;
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

	@Override
	public boolean equals(Object o) {
		try {
			Category c = (Category) o;
			return this.id == c.id;
		}catch (ClassCastException e){
			return false;
		}
	}

	@Override
 	public String toString(){
		return 
			"Category{" + 
			"parent = '" + parent + '\'' +
			",count = '" + count + '\'' +
			",name = '" + name + '\'' +
			",id = '" + id + '\'' +
			"}";
		}
}