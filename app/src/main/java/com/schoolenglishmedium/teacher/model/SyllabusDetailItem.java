package com.schoolenglishmedium.teacher.model;

import java.util.List;
import com.google.gson.annotations.SerializedName;

public class SyllabusDetailItem{

	@SerializedName("subject_id")
	private String subjectId;

	@SerializedName("subject_code")
	private String subjectCode;

	@SerializedName("section_name")
	private String sectionName;

	@SerializedName("class_id")
	private String classId;

	@SerializedName("document")
	private List<DocumentItem> document;

	@SerializedName("session_id")
	private String sessionId;

	@SerializedName("created_at")
	private String createdAt;

	@SerializedName("title")
	private String title;

	@SerializedName("created_by")
	private String createdBy;

	@SerializedName("deleted_at")
	private String deletedAt;

	@SerializedName("section_id")
	private String sectionId;

	@SerializedName("updated_at")
	private String updatedAt;

	@SerializedName("subject_name")
	private String subjectName;

	@SerializedName("id")
	private String id;

	@SerializedName("class_name")
	private String className;

	public String getSubjectId(){
		return subjectId;
	}

	public String getSubjectCode(){
		return subjectCode;
	}

	public String getSectionName(){
		return sectionName;
	}

	public String getClassId(){
		return classId;
	}

	public List<DocumentItem> getDocument(){
		return document;
	}

	public String getSessionId(){
		return sessionId;
	}

	public String getCreatedAt(){
		return createdAt;
	}

	public String getTitle(){
		return title;
	}

	public String getCreatedBy(){
		return createdBy;
	}

	public String getDeletedAt(){
		return deletedAt;
	}

	public String getSectionId(){
		return sectionId;
	}

	public String getUpdatedAt(){
		return updatedAt;
	}

	public String getSubjectName(){
		return subjectName;
	}

	public String getId(){
		return id;
	}

	public String getClassName(){
		return className;
	}

	@Override
 	public String toString(){
		return 
			"SyllabusDetailItem{" + 
			"subject_id = '" + subjectId + '\'' + 
			",subject_code = '" + subjectCode + '\'' + 
			",section_name = '" + sectionName + '\'' + 
			",class_id = '" + classId + '\'' + 
			",document = '" + document + '\'' + 
			",session_id = '" + sessionId + '\'' + 
			",created_at = '" + createdAt + '\'' + 
			",title = '" + title + '\'' + 
			",created_by = '" + createdBy + '\'' + 
			",deleted_at = '" + deletedAt + '\'' + 
			",section_id = '" + sectionId + '\'' + 
			",updated_at = '" + updatedAt + '\'' + 
			",subject_name = '" + subjectName + '\'' + 
			",id = '" + id + '\'' + 
			",class_name = '" + className + '\'' + 
			"}";
		}
}