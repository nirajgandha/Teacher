package com.schoolenglishmedium.teacher.model;

import java.util.ArrayList;
import com.google.gson.annotations.SerializedName;

public class GetSyllabusResponse{

	@SerializedName("data")
	private ArrayList<Syllabus> data;

	@SerializedName("meta")
	private Meta meta;

	public ArrayList<Syllabus> getData(){
		return data;
	}

	public Meta getMeta(){
		return meta;
	}

	@Override
 	public String toString(){
		return 
			"GetSyllabusResponse{" + 
			"data = '" + data + '\'' + 
			",meta = '" + meta + '\'' + 
			"}";
		}
}