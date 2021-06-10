package com.schoolenglishmedium.teacher.model;

import com.google.gson.annotations.SerializedName;

public class ActivityNotesDetailItem{

	@SerializedName("display_note")
	private String displayNote;

	@SerializedName("note")
	private String note;

	@SerializedName("teacher_name")
	private String teacherName;

	@SerializedName("updated_at")
	private String updatedAt;

	@SerializedName("teacher_id")
	private String teacherId;

	@SerializedName("created_at")
	private String createdAt;

	@SerializedName("teacher_activity_id")
	private String teacherActivityId;

	@SerializedName("id")
	private String id;

	@SerializedName("deleted_at")
	private String deletedAt;

	public String getDisplayNote(){
		return displayNote;
	}

	public String getNote(){
		return note;
	}

	public String getTeacherName(){
		return teacherName;
	}

	public String getUpdatedAt(){
		return updatedAt;
	}

	public String getTeacherId(){
		return teacherId;
	}

	public String getCreatedAt(){
		return createdAt;
	}

	public String getTeacherActivityId(){
		return teacherActivityId;
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
			"ActivityNotesDetailItem{" + 
			"display_note = '" + displayNote + '\'' +
			"note = '" + note + '\'' +
			",teacher_name = '" + teacherName + '\'' +
			",updated_at = '" + updatedAt + '\'' + 
			",teacher_id = '" + teacherId + '\'' + 
			",created_at = '" + createdAt + '\'' + 
			",teacher_activity_id = '" + teacherActivityId + '\'' + 
			",id = '" + id + '\'' + 
			",deleted_at = '" + deletedAt + '\'' + 
			"}";
		}
}