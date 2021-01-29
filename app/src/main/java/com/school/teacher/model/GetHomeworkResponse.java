package com.school.teacher.model;

import java.util.*;

import com.google.gson.annotations.SerializedName;

public class GetHomeworkResponse{

	@SerializedName("data")
	private ArrayList<Homework> data;

	@SerializedName("meta")
	private Meta meta;

	public ArrayList<Homework> getData(){
		return data;
	}

	public Meta getMeta(){
		return meta;
	}

	@Override
 	public String toString(){
		return 
			"GetHomeworkResponse{" + 
			"data = '" + data + '\'' + 
			",meta = '" + meta + '\'' + 
			"}";
		}
}