package com.schoolenglishmedium.teacher.model;

import com.google.gson.annotations.SerializedName;

public class TeacherActivityNotesResponse{

	@SerializedName("data")
	private TeacherActivityNoteData teacherActivityNoteData;

	@SerializedName("meta")
	private Meta meta;

	public TeacherActivityNoteData getTeacherActivityNoteData(){
		return teacherActivityNoteData;
	}

	public Meta getMeta(){
		return meta;
	}

	@Override
 	public String toString(){
		return 
			"TeacherActivityNotesResponse{" + 
			"data = '" + teacherActivityNoteData + '\'' +
			",meta = '" + meta + '\'' + 
			"}";
		}
}