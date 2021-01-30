package com.school.teacher.model;

import java.util.ArrayList;
import com.google.gson.annotations.SerializedName;

public class SubjectResponse{

	@SerializedName("data")
	private ArrayList<SubjectItem> data;

	@SerializedName("meta")
	private Meta meta;

	public ArrayList<SubjectItem> getData(){
		return data;
	}

	public Meta getMeta(){
		return meta;
	}

	@Override
 	public String toString(){
		return 
			"SubjectResponse{" + 
			"data = '" + data + '\'' + 
			",meta = '" + meta + '\'' + 
			"}";
		}
}