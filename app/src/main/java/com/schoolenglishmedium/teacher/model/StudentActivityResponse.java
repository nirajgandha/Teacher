package com.schoolenglishmedium.teacher.model;

import java.util.ArrayList;
import com.google.gson.annotations.SerializedName;

public class StudentActivityResponse {

	@SerializedName("data")
	private ArrayList<StudentActivityDetail> data;

	@SerializedName("meta")
	private Meta meta;

	public ArrayList<StudentActivityDetail> getData(){
		return data;
	}

	public Meta getMeta(){
		return meta;
	}

	@Override
 	public String toString(){
		return 
			"AddStudentActivityResponse{" + 
			"data = '" + data + '\'' + 
			",meta = '" + meta + '\'' + 
			"}";
		}
}