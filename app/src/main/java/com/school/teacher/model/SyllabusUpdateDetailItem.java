package com.school.teacher.model;

import com.google.gson.annotations.SerializedName;

public class SyllabusUpdateDetailItem{

	@SerializedName("subject_id")
	private String subjectId;

	@SerializedName("end_date")
	private String endDate;

	@SerializedName("subject_code")
	private String subjectCode;

	@SerializedName("teacher_id")
	private String teacherId;

	@SerializedName("section_name")
	private String sectionName;

	@SerializedName("class_id")
	private String classId;

	@SerializedName("description")
	private String description;

	@SerializedName("created_at")
	private String createdAt;

	@SerializedName("remark")
	private String remark;

	@SerializedName("title")
	private String title;

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

	@SerializedName("syllabus_id")
	private String syllabusId;

	@SerializedName("class_name")
	private String className;

	@SerializedName("start_date")
	private String startDate;

	public String getSubjectId(){
		return subjectId;
	}

	public String getEndDate(){
		return endDate;
	}

	public String getSubjectCode(){
		return subjectCode;
	}

	public String getTeacherId(){
		return teacherId;
	}

	public String getSectionName(){
		return sectionName;
	}

	public String getClassId(){
		return classId;
	}

	public String getDescription(){
		return description;
	}

	public String getCreatedAt(){
		return createdAt;
	}

	public String getRemark(){
		return remark;
	}

	public String getTitle(){
		return title;
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

	public String getSyllabusId(){
		return syllabusId;
	}

	public String getClassName(){
		return className;
	}

	public String getStartDate(){
		return startDate;
	}

	@Override
 	public String toString(){
		return 
			"SyllabusUpdateDetailItem{" + 
			"subject_id = '" + subjectId + '\'' + 
			",end_date = '" + endDate + '\'' + 
			",subject_code = '" + subjectCode + '\'' + 
			",teacher_id = '" + teacherId + '\'' + 
			",section_name = '" + sectionName + '\'' + 
			",class_id = '" + classId + '\'' + 
			",description = '" + description + '\'' + 
			",created_at = '" + createdAt + '\'' + 
			",remark = '" + remark + '\'' + 
			",title = '" + title + '\'' + 
			",deleted_at = '" + deletedAt + '\'' + 
			",section_id = '" + sectionId + '\'' + 
			",updated_at = '" + updatedAt + '\'' + 
			",subject_name = '" + subjectName + '\'' + 
			",id = '" + id + '\'' + 
			",syllabus_id = '" + syllabusId + '\'' + 
			",class_name = '" + className + '\'' + 
			",start_date = '" + startDate + '\'' + 
			"}";
		}
}