package com.schoolenglishmedium.teacher.model;

import java.util.ArrayList;
import com.google.gson.annotations.SerializedName;

public class ClassListResponse{

	@SerializedName("data")
	private ArrayList<ClassList> data;

	@SerializedName("meta")
	private Meta meta;

	public ArrayList<ClassList> getData(){
		return data;
	}

	public Meta getMeta(){
		return meta;
	}

	@Override
 	public String toString(){
		return 
			"ClassListResponse{" + 
			"data = '" + data + '\'' + 
			",meta = '" + meta + '\'' + 
			"}";
		}
}