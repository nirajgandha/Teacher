package com.school.teacher.model;

import com.google.gson.annotations.SerializedName;

public class ClassList {

	@SerializedName("is_active")
	private String isActive;

	@SerializedName("updated_at")
	private String updatedAt;

	@SerializedName("created_at")
	private String createdAt;

	@SerializedName("id")
	private String id;

	@SerializedName("class")
	private String mMemberClass;

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

	public String getId(){
		return id;
	}

	public String getMmemberClass(){
		return mMemberClass;
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
			",id = '" + id + '\'' + 
			",class = '" + mMemberClass + '\'' +
			",deleted_at = '" + deletedAt + '\'' + 
			"}";
		}
}