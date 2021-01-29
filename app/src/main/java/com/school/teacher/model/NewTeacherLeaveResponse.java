package com.school.teacher.model;

import com.google.gson.annotations.SerializedName;

public class NewTeacherLeaveResponse{

	@SerializedName("data")
	private NewTeacherLeaveData newTeacherLeaveData;

	@SerializedName("meta")
	private Meta meta;

	public NewTeacherLeaveData getNewTeacherLeaveData(){
		return newTeacherLeaveData;
	}

	public Meta getMeta(){
		return meta;
	}

	@Override
 	public String toString(){
		return 
			"NewTeacherLeaveResponse{" + 
			"data = '" + newTeacherLeaveData + '\'' +
			",meta = '" + meta + '\'' + 
			"}";
		}
}