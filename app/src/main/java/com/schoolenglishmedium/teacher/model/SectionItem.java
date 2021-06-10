package com.schoolenglishmedium.teacher.model;

import com.google.gson.annotations.SerializedName;

public class SectionItem {

	@SerializedName("is_active")
	private String isActive;

	@SerializedName("updated_at")
	private String updatedAt;

	@SerializedName("created_at")
	private String createdAt;

	@SerializedName("section")
	private String section;

	@SerializedName("id")
	private String id;

	@SerializedName("deleted_at")
	private String deletedAt;

	public String getIsActive(){
		return isActive;
	}

	public String getUpdatedAt(){
		return updatedAt;
	}

	public String getCreatedAt(){
		return createdAt;
	}

	public String getSection(){
		return section;
	}

	public String getId(){
		return id;
	}

	public String getDeletedAt(){
		return deletedAt;
	}

	@Override
 	public String toString(){
		return 
			"DataItem{" + 
			"is_active = '" + isActive + '\'' + 
			",updated_at = '" + updatedAt + '\'' + 
			",created_at = '" + createdAt + '\'' + 
			",section = '" + section + '\'' + 
			",id = '" + id + '\'' + 
			",deleted_at = '" + deletedAt + '\'' + 
			"}";
		}
}