package com.school.teacher.model;

import com.google.gson.annotations.SerializedName;

public class Attendance {

	@SerializedName("date")
	private String date;

	@SerializedName("section_id")
	private String sectionId;

	@SerializedName("updated_at")
	private String updatedAt;

	@SerializedName("class_id")
	private String classId;

	@SerializedName("student_id")
	private String studentId;

	@SerializedName("session_id")
	private String sessionId;

	@SerializedName("created_at")
	private String createdAt;

	@SerializedName("attendence_type_id")
	private String attendenceTypeId;

	@SerializedName("remark")
	private String remark;

	@SerializedName("id")
	private String id;

	@SerializedName("title_name")
	private String titleName;

	@SerializedName("deleted_at")
	private String deletedAt;

	public String getDate(){
		return date;
	}

	public String getSectionId(){
		return sectionId;
	}

	public String getUpdatedAt(){
		return updatedAt;
	}

	public String getClassId(){
		return classId;
	}

	public String getStudentId(){
		return studentId;
	}

	public String getSessionId(){
		return sessionId;
	}

	public String getCreatedAt(){
		return createdAt;
	}

	public String getAttendenceTypeId(){
		return attendenceTypeId;
	}

	public String getRemark(){
		return remark;
	}

	public String getId(){
		return id;
	}

	public String getTitleName(){
		return titleName;
	}

	public String getDeletedAt(){
		return deletedAt;
	}

	@Override
 	public String toString(){
		return 
			"Attendance{" +
			"date = '" + date + '\'' + 
			",section_id = '" + sectionId + '\'' + 
			",updated_at = '" + updatedAt + '\'' + 
			",class_id = '" + classId + '\'' + 
			",student_id = '" + studentId + '\'' + 
			",session_id = '" + sessionId + '\'' + 
			",created_at = '" + createdAt + '\'' + 
			",attendence_type_id = '" + attendenceTypeId + '\'' + 
			",remark = '" + remark + '\'' + 
			",id = '" + id + '\'' + 
			",title_name = '" + titleName + '\'' + 
			",deleted_at = '" + deletedAt + '\'' + 
			"}";
		}
}