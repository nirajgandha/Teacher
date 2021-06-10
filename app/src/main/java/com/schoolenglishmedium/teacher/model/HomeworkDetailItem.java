package com.schoolenglishmedium.teacher.model;

import com.google.gson.annotations.SerializedName;

public class HomeworkDetailItem{

	@SerializedName("subject_id")
	private String subjectId;

	@SerializedName("evaluated_by")
	private String evaluatedBy;

	@SerializedName("homework_teacher_comment")
	private String homeworkTeacherComment;

	@SerializedName("section_name")
	private String sectionName;

	@SerializedName("class_id")
	private String classId;

	@SerializedName("document")
	private String document;

	@SerializedName("evaluated_by_name")
	private String evaluatedByName;

	@SerializedName("session_id")
	private String sessionId;

	@SerializedName("description")
	private String description;

	@SerializedName("created_at")
	private String createdAt;

	@SerializedName("created_by_name")
	private String createdByName;

	@SerializedName("student_ids")
	private String studentIds;

	@SerializedName("created_by")
	private String createdBy;

	@SerializedName("deleted_at")
	private String deletedAt;

	@SerializedName("homework_date")
	private String homeworkDate;

	@SerializedName("section_id")
	private String sectionId;

	@SerializedName("updated_at")
	private String updatedAt;

	@SerializedName("staff_id")
	private String staffId;

	@SerializedName("subject_name")
	private String subjectName;

	@SerializedName("id")
	private String id;

	@SerializedName("submit_date")
	private String submitDate;

	@SerializedName("create_date")
	private String createDate;

	@SerializedName("evaluation_date")
	private String evaluationDate;

	@SerializedName("class_name")
	private String className;

	public String getSubjectId(){
		return subjectId;
	}

	public String getEvaluatedBy(){
		return evaluatedBy;
	}

	public String getHomeworkTeacherComment(){
		return homeworkTeacherComment;
	}

	public String getSectionName(){
		return sectionName;
	}

	public String getClassId(){
		return classId;
	}

	public String getDocument(){
		return document;
	}

	public String getEvaluatedByName(){
		return evaluatedByName;
	}

	public String getSessionId(){
		return sessionId;
	}

	public String getDescription(){
		return description;
	}

	public String getCreatedAt(){
		return createdAt;
	}

	public String getCreatedByName(){
		return createdByName;
	}

	public String getStudentIds(){
		return studentIds;
	}

	public String getCreatedBy(){
		return createdBy;
	}

	public String getDeletedAt(){
		return deletedAt;
	}

	public String getHomeworkDate(){
		return homeworkDate;
	}

	public String getSectionId(){
		return sectionId;
	}

	public String getUpdatedAt(){
		return updatedAt;
	}

	public String getStaffId(){
		return staffId;
	}

	public String getSubjectName(){
		return subjectName;
	}

	public String getId(){
		return id;
	}

	public String getSubmitDate(){
		return submitDate;
	}

	public String getCreateDate(){
		return createDate;
	}

	public String getEvaluationDate(){
		return evaluationDate;
	}

	public String getClassName(){
		return className;
	}

	@Override
 	public String toString(){
		return 
			"HomeworkDetailItem{" + 
			"subject_id = '" + subjectId + '\'' + 
			",evaluated_by = '" + evaluatedBy + '\'' + 
			",homework_teacher_comment = '" + homeworkTeacherComment + '\'' + 
			",section_name = '" + sectionName + '\'' + 
			",class_id = '" + classId + '\'' + 
			",document = '" + document + '\'' + 
			",evaluated_by_name = '" + evaluatedByName + '\'' + 
			",session_id = '" + sessionId + '\'' + 
			",description = '" + description + '\'' + 
			",created_at = '" + createdAt + '\'' + 
			",created_by_name = '" + createdByName + '\'' + 
			",student_ids = '" + studentIds + '\'' + 
			",created_by = '" + createdBy + '\'' + 
			",deleted_at = '" + deletedAt + '\'' + 
			",homework_date = '" + homeworkDate + '\'' + 
			",section_id = '" + sectionId + '\'' + 
			",updated_at = '" + updatedAt + '\'' + 
			",staff_id = '" + staffId + '\'' + 
			",subject_name = '" + subjectName + '\'' + 
			",id = '" + id + '\'' + 
			",submit_date = '" + submitDate + '\'' + 
			",create_date = '" + createDate + '\'' + 
			",evaluation_date = '" + evaluationDate + '\'' + 
			",class_name = '" + className + '\'' + 
			"}";
		}
}