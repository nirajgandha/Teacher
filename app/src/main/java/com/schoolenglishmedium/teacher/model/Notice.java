package com.schoolenglishmedium.teacher.model;

import com.google.gson.annotations.SerializedName;

public class Notice {

	@SerializedName("date")
	private String date;

	@SerializedName("visible_student")
	private String visibleStudent;

	@SerializedName("visible_teacher")
	private String visibleTeacher;

	@SerializedName("created_at")
	private String createdAt;

	@SerializedName("title")
	private String title;

	@SerializedName("message")
	private String message;

	@SerializedName("type")
	private String type;

	@SerializedName("created_by")
	private String createdBy;

	@SerializedName("deleted_at")
	private String deletedAt;

	@SerializedName("updated_at")
	private String updatedAt;

	@SerializedName("visible_gujarati_principle")
	private String visibleGujaratiPrinciple;

	@SerializedName("visible_english_principle")
	private String visibleEnglishPrinciple;

	@SerializedName("visible_staff")
	private String visibleStaff;

	@SerializedName("id")
	private String id;

	@SerializedName("publish_date")
	private String publishDate;

	@SerializedName("student_id")
	private String studentId;

	public String getDate(){
		return date;
	}

	public String getVisibleStudent(){
		return visibleStudent;
	}

	public String getVisibleTeacher(){
		return visibleTeacher;
	}

	public String getCreatedAt(){
		return createdAt;
	}

	public String getTitle(){
		return title;
	}

	public String getMessage(){
		return message;
	}

	public String getType(){
		return type;
	}

	public String getCreatedBy(){
		return createdBy;
	}

	public String getDeletedAt(){
		return deletedAt;
	}

	public String getUpdatedAt(){
		return updatedAt;
	}

	public String getVisibleGujaratiPrinciple(){
		return visibleGujaratiPrinciple;
	}

	public String getVisibleEnglishPrinciple(){
		return visibleEnglishPrinciple;
	}

	public String getVisibleStaff(){
		return visibleStaff;
	}

	public String getId(){
		return id;
	}

	public String getPublishDate(){
		return publishDate;
	}

	public String getStudentId(){
		return studentId;
	}

	@Override
 	public String toString(){
		return 
			"Notice{" +
			"date = '" + date + '\'' + 
			",visible_student = '" + visibleStudent + '\'' + 
			",visible_teacher = '" + visibleTeacher + '\'' + 
			",created_at = '" + createdAt + '\'' + 
			",title = '" + title + '\'' + 
			",message = '" + message + '\'' + 
			",type = '" + type + '\'' + 
			",created_by = '" + createdBy + '\'' + 
			",deleted_at = '" + deletedAt + '\'' + 
			",updated_at = '" + updatedAt + '\'' + 
			",visible_gujarati_principle = '" + visibleGujaratiPrinciple + '\'' + 
			",visible_english_principle = '" + visibleEnglishPrinciple + '\'' + 
			",visible_staff = '" + visibleStaff + '\'' + 
			",id = '" + id + '\'' + 
			",publish_date = '" + publishDate + '\'' + 
			",student_id = '" + studentId + '\'' +
			"}";
		}
}