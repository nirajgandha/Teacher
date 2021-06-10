package com.schoolenglishmedium.teacher.model;

import java.util.List;
import com.google.gson.annotations.SerializedName;

public class HomeworkItem {

	@SerializedName("teacher_comment")
	private String teacherComment;

	@SerializedName("updated_at")
	private String updatedAt;

	@SerializedName("teacher_id")
	private String teacherId;

	@SerializedName("homework_id")
	private String homeworkId;

	@SerializedName("student_id")
	private String studentId;

	@SerializedName("created_at")
	private String createdAt;

	@SerializedName("id")
	private String id;

	@SerializedName("upload_files")
	private List<UploadFilesItem> uploadFiles;

	@SerializedName("deleted_at")
	private String deletedAt;

	@SerializedName("status")
	private String status;

	public String getTeacherComment(){
		return teacherComment;
	}

	public String getUpdatedAt(){
		return updatedAt;
	}

	public String getTeacherId(){
		return teacherId;
	}

	public String getHomeworkId(){
		return homeworkId;
	}

	public String getStudentId(){
		return studentId;
	}

	public String getCreatedAt(){
		return createdAt;
	}

	public String getId(){
		return id;
	}

	public List<UploadFilesItem> getUploadFiles(){
		return uploadFiles;
	}

	public String getDeletedAt(){
		return deletedAt;
	}

	public String getStatus(){
		return status;
	}

	@Override
 	public String toString(){
		return 
			"HomeworkItem{" +
			"teacher_comment = '" + teacherComment + '\'' + 
			",updated_at = '" + updatedAt + '\'' + 
			",teacher_id = '" + teacherId + '\'' + 
			",homework_id = '" + homeworkId + '\'' + 
			",student_id = '" + studentId + '\'' + 
			",created_at = '" + createdAt + '\'' + 
			",id = '" + id + '\'' + 
			",upload_files = '" + uploadFiles + '\'' + 
			",deleted_at = '" + deletedAt + '\'' + 
			",status = '" + status + '\'' + 
			"}";
		}
}