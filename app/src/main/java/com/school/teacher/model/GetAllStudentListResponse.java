package com.school.teacher.model;

import com.google.gson.annotations.SerializedName;

public class GetAllStudentListResponse{

	@SerializedName("data")
	private GetAllStudentListData getAllStudentListData;

	@SerializedName("meta")
	private Meta meta;

	public GetAllStudentListData getGetAllStudentListData(){
		return getAllStudentListData;
	}

	public Meta getMeta(){
		return meta;
	}

	@Override
 	public String toString(){
		return 
			"GetAllStudentListResponse{" + 
			"data = '" + getAllStudentListData + '\'' +
			",meta = '" + meta + '\'' + 
			"}";
		}
}