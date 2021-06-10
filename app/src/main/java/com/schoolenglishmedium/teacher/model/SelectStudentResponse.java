package com.schoolenglishmedium.teacher.model;

import java.util.*;

import com.google.gson.annotations.SerializedName;

public class SelectStudentResponse{

	@SerializedName("data")
	private ArrayList<SelectStudent> data;

	@SerializedName("meta")
	private Meta meta;

	public ArrayList<SelectStudent> getData(){
		return data;
	}

	public Meta getMeta(){
		return meta;
	}

	@Override
 	public String toString(){
		return 
			"SelectStudentResponse{" + 
			"data = '" + data + '\'' + 
			",meta = '" + meta + '\'' + 
			"}";
		}
}