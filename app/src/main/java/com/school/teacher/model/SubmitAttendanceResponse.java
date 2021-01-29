package com.school.teacher.model;

import com.google.gson.annotations.SerializedName;

public class SubmitAttendanceResponse{

	@SerializedName("data")
	private SubmitAttendanceData data;

	@SerializedName("meta")
	private Meta meta;

	public SubmitAttendanceData getData(){
		return data;
	}

	public Meta getMeta(){
		return meta;
	}

	@Override
 	public String toString(){
		return 
			"SubmitAttendanceResponse{" + 
			"data = '" + data + '\'' + 
			",meta = '" + meta + '\'' + 
			"}";
		}
}