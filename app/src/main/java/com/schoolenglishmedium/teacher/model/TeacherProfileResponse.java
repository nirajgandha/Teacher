package com.schoolenglishmedium.teacher.model;

import java.util.ArrayList;
import com.google.gson.annotations.SerializedName;

public class TeacherProfileResponse{

	@SerializedName("data")
	private ArrayList<TeacherProfileData> data;

	@SerializedName("meta")
	private Meta meta;

	public ArrayList<TeacherProfileData> getData(){
		return data;
	}

	public Meta getMeta(){
		return meta;
	}

	@Override
 	public String toString(){
		return 
			"TeacherProfileResponse{" + 
			"data = '" + data + '\'' + 
			",meta = '" + meta + '\'' + 
			"}";
		}
}