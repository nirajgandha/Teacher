package com.school.teacher.model;

import com.google.gson.annotations.SerializedName;

public class UpdateHomeworkResponse{

	@SerializedName("data")
	private UpdateHomeworkData updateHomeworkData;

	@SerializedName("meta")
	private Meta meta;

	public UpdateHomeworkData getUpdateHomeworkData(){
		return updateHomeworkData;
	}

	public Meta getMeta(){
		return meta;
	}

	@Override
 	public String toString(){
		return 
			"UpdateHomeworkResponse{" + 
			"data = '" + updateHomeworkData + '\'' +
			",meta = '" + meta + '\'' + 
			"}";
		}
}