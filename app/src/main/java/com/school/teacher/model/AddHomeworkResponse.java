package com.school.teacher.model;

import com.google.gson.annotations.SerializedName;

public class AddHomeworkResponse{

	@SerializedName("data")
	private int data;

	@SerializedName("meta")
	private Meta meta;

	public int getData(){
		return data;
	}

	public Meta getMeta(){
		return meta;
	}

	@Override
 	public String toString(){
		return 
			"AddHomeworkResponse{" + 
			"data = '" + data + '\'' + 
			",meta = '" + meta + '\'' + 
			"}";
		}
}