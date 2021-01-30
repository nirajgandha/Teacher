package com.school.teacher.model;

import com.google.gson.annotations.SerializedName;

public class SubjectItem {

	@SerializedName("code")
	private String code;

	@SerializedName("is_active")
	private String isActive;

	@SerializedName("updated_at")
	private String updatedAt;

	@SerializedName("name")
	private String name;

	@SerializedName("created_at")
	private String createdAt;

	@SerializedName("id")
	private String id;

	@SerializedName("type")
	private String type;

	@SerializedName("deleted_at")
	private String deletedAt;

	public String getCode(){
		return code;
	}

	public String getIsActive(){
		return isActive;
	}

	public String getUpdatedAt(){
		return updatedAt;
	}

	public String getName(){
		return name;
	}

	public String getCreatedAt(){
		return createdAt;
	}

	public String getId(){
		return id;
	}

	public String getType(){
		return type;
	}

	public String getDeletedAt(){
		return deletedAt;
	}

	@Override
 	public String toString(){
		return 
			"DataItem{" + 
			"code = '" + code + '\'' + 
			",is_active = '" + isActive + '\'' + 
			",updated_at = '" + updatedAt + '\'' + 
			",name = '" + name + '\'' + 
			",created_at = '" + createdAt + '\'' + 
			",id = '" + id + '\'' + 
			",type = '" + type + '\'' + 
			",deleted_at = '" + deletedAt + '\'' + 
			"}";
		}
}