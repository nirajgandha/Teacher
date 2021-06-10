package com.schoolenglishmedium.teacher.model;

import java.util.*;

import com.google.gson.annotations.SerializedName;

public class FoodMenu {

	@SerializedName("updated_at")
	private String updatedAt;

	@SerializedName("document")
	private ArrayList<DocumentItem> document;

	@SerializedName("description")
	private String description;

	@SerializedName("created_at")
	private String createdAt;

	@SerializedName("id")
	private String id;

	@SerializedName("title")
	private String title;

	@SerializedName("deleted_at")
	private String deletedAt;

	public String getUpdatedAt(){
		return updatedAt;
	}

	public ArrayList<DocumentItem> getDocument(){
		return document;
	}

	public String getDescription(){
		return description;
	}

	public String getCreatedAt(){
		return createdAt;
	}

	public String getId(){
		return id;
	}

	public String getTitle(){
		return title;
	}

	public String getDeletedAt(){
		return deletedAt;
	}

	@Override
 	public String toString(){
		return 
			"FoodMenu{" +
			"updated_at = '" + updatedAt + '\'' + 
			",document = '" + document + '\'' + 
			",description = '" + description + '\'' + 
			",created_at = '" + createdAt + '\'' + 
			",id = '" + id + '\'' + 
			",title = '" + title + '\'' + 
			",deleted_at = '" + deletedAt + '\'' + 
			"}";
		}
}