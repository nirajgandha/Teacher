package com.schoolenglishmedium.teacher.model;

import java.util.ArrayList;
import com.google.gson.annotations.SerializedName;

public class TeacherLeaveResponse{

	@SerializedName("data")
	private ArrayList<TeacherLeaveData> data;

	@SerializedName("meta")
	private Meta meta;

	public ArrayList<TeacherLeaveData> getData(){
		return data;
	}

	public Meta getMeta(){
		return meta;
	}

	@Override
 	public String toString(){
		return 
			"TeacherLeaveResponse{" + 
			"data = '" + data + '\'' + 
			",meta = '" + meta + '\'' + 
			"}";
		}
}