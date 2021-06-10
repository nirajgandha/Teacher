package com.schoolenglishmedium.teacher.model;

import com.google.gson.annotations.SerializedName;

public class AttendanceListResponse{

	@SerializedName("data")
	private AttendanceListData data;

	@SerializedName("meta")
	private Meta meta;

	public AttendanceListData getData(){
		return data;
	}

	public Meta getMeta(){
		return meta;
	}

	@Override
 	public String toString(){
		return 
			"AttendanceListResponse{" + 
			"data = '" + data + '\'' + 
			",meta = '" + meta + '\'' + 
			"}";
		}
}