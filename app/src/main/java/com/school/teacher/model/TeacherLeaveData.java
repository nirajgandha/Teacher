package com.school.teacher.model;

import com.google.gson.annotations.SerializedName;

public class TeacherLeaveData {

	@SerializedName("reason")
	private String reason;

	@SerializedName("teacher_name")
	private String teacherName;

	@SerializedName("from_date")
	private String fromDate;

	@SerializedName("request_type")
	private String requestType;

	@SerializedName("teacher_id")
	private String teacherId;

	@SerializedName("student_id")
	private String studentId;

	@SerializedName("created_at")
	private String createdAt;

	@SerializedName("deleted_at")
	private String deletedAt;

	@SerializedName("approve_by")
	private String approveBy;

	@SerializedName("apply_date")
	private String applyDate;

	@SerializedName("to_date")
	private String toDate;

	@SerializedName("docs")
	private String docs;

	@SerializedName("updated_at")
	private String updatedAt;

	@SerializedName("approve_by_name")
	private String approveByName;

	@SerializedName("id")
	private String id;

	public String getReason(){
		return reason;
	}

	public String getTeacherName(){
		return teacherName;
	}

	public String getFromDate(){
		return fromDate;
	}

	public String getRequestType(){
		return requestType;
	}

	public String getTeacherId(){
		return teacherId;
	}

	public String getStudentId(){
		return studentId;
	}

	public String getCreatedAt(){
		return createdAt;
	}

	public String getDeletedAt(){
		return deletedAt;
	}

	public String getApproveBy(){
		return approveBy;
	}

	public String getApplyDate(){
		return applyDate;
	}

	public String getToDate(){
		return toDate;
	}

	public String getDocs(){
		return docs;
	}

	public String getUpdatedAt(){
		return updatedAt;
	}

	public String getApproveByName(){
		return approveByName;
	}

	public String getId(){
		return id;
	}

	@Override
 	public String toString(){
		return 
			"DataItem{" + 
			"reason = '" + reason + '\'' + 
			",teacher_name = '" + teacherName + '\'' + 
			",from_date = '" + fromDate + '\'' + 
			",request_type = '" + requestType + '\'' + 
			",teacher_id = '" + teacherId + '\'' + 
			",student_id = '" + studentId + '\'' + 
			",created_at = '" + createdAt + '\'' + 
			",deleted_at = '" + deletedAt + '\'' + 
			",approve_by = '" + approveBy + '\'' + 
			",apply_date = '" + applyDate + '\'' + 
			",to_date = '" + toDate + '\'' + 
			",docs = '" + docs + '\'' + 
			",updated_at = '" + updatedAt + '\'' + 
			",approve_by_name = '" + approveByName + '\'' + 
			",id = '" + id + '\'' + 
			"}";
		}
}