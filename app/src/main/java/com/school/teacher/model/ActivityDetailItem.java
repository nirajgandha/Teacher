package com.school.teacher.model;

import com.google.gson.annotations.SerializedName;

public class ActivityDetailItem{

	@SerializedName("date")
	private String date;

	@SerializedName("teacher_name")
	private String teacherName;

	@SerializedName("updated_at")
	private String updatedAt;

	@SerializedName("instruction")
	private String instruction;

	@SerializedName("teacher_id")
	private String teacherId;

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

	public String getDate(){
		return date;
	}

	public String getTeacherName(){
		return teacherName;
	}

	public String getUpdatedAt(){
		return updatedAt;
	}

	public String getInstruction(){
		return instruction;
	}

	public String getTeacherId(){
		return teacherId;
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
			"ActivityDetailItem{" + 
			"date = '" + date + '\'' + 
			",teacher_name = '" + teacherName + '\'' + 
			",updated_at = '" + updatedAt + '\'' + 
			",instruction = '" + instruction + '\'' + 
			",teacher_id = '" + teacherId + '\'' + 
			",description = '" + description + '\'' + 
			",created_at = '" + createdAt + '\'' + 
			",id = '" + id + '\'' + 
			",title = '" + title + '\'' + 
			",deleted_at = '" + deletedAt + '\'' + 
			"}";
		}
}