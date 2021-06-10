package com.schoolenglishmedium.teacher.model;

import java.util.ArrayList;
import com.google.gson.annotations.SerializedName;

public class HomeworkUpdateDetailItem{

	@SerializedName("homework_teacher_comment")
	private String homeworkTeacherComment;

	@SerializedName("teacher_id")
	private String teacherId;

	@SerializedName("homework_id")
	private String homeworkId;

	@SerializedName("homework_status")
	private String homeworkStatus;

	@SerializedName("evaluated_by_name")
	private String evaluatedByName;

	@SerializedName("student_id")
	private String studentId;

	@SerializedName("created_at")
	private String createdAt;

	@SerializedName("upload_files")
	private ArrayList<String> uploadFiles;

	@SerializedName("deleted_at")
	private String deletedAt;

	@SerializedName("student_name")
	private String studentName;

	@SerializedName("teacher_comment")
	private String teacherComment;

	@SerializedName("updated_at")
	private String updatedAt;

	@SerializedName("id")
	private String id;

	@SerializedName("status")
	private String status;

	public String getHomeworkTeacherComment(){
		return homeworkTeacherComment;
	}

	public String getTeacherId(){
		return teacherId;
	}

	public String getHomeworkId(){
		return homeworkId;
	}

	public String getHomeworkStatus(){
		return homeworkStatus;
	}

	public String getEvaluatedByName(){
		return evaluatedByName;
	}

	public String getStudentId(){
		return studentId;
	}

	public String getCreatedAt(){
		return createdAt;
	}

	public ArrayList<String> getUploadFiles(){
		return uploadFiles;
	}

	public String getDeletedAt(){
		return deletedAt;
	}

	public String getStudentName(){
		return studentName;
	}

	public String getTeacherComment(){
		return teacherComment;
	}

	public String getUpdatedAt(){
		return updatedAt;
	}

	public String getId(){
		return id;
	}

	public String getStatus(){
		return status;
	}

	@Override
 	public String toString(){
		return 
			"HomeworkUpdateDetailItem{" + 
			"homework_teacher_comment = '" + homeworkTeacherComment + '\'' + 
			",teacher_id = '" + teacherId + '\'' + 
			",homework_id = '" + homeworkId + '\'' + 
			",homework_status = '" + homeworkStatus + '\'' + 
			",evaluated_by_name = '" + evaluatedByName + '\'' + 
			",student_id = '" + studentId + '\'' + 
			",created_at = '" + createdAt + '\'' + 
			",upload_files = '" + uploadFiles + '\'' + 
			",deleted_at = '" + deletedAt + '\'' + 
			",student_name = '" + studentName + '\'' + 
			",teacher_comment = '" + teacherComment + '\'' + 
			",updated_at = '" + updatedAt + '\'' + 
			",id = '" + id + '\'' + 
			",status = '" + status + '\'' + 
			"}";
		}
}