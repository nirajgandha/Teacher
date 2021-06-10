package com.schoolenglishmedium.teacher.model;

import com.google.gson.annotations.SerializedName;

public class SyllabusUpdateDetailResponse{

	@SerializedName("data")
	private SyllabusUpdateDetailData data;

	@SerializedName("meta")
	private Meta meta;

	public SyllabusUpdateDetailData getData(){
		return data;
	}

	public Meta getMeta(){
		return meta;
	}

	@Override
 	public String toString(){
		return 
			"SyllabusUpdateDetailResponse{" + 
			"data = '" + data + '\'' + 
			",meta = '" + meta + '\'' + 
			"}";
		}
}