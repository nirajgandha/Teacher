package com.school.teacher.model;

import com.google.gson.annotations.SerializedName;

public class HomeWorkUpdateResponse{

	@SerializedName("data")
	private HomeWorkUpdateData data;

	@SerializedName("meta")
	private Meta meta;

	public HomeWorkUpdateData getData(){
		return data;
	}

	public Meta getMeta(){
		return meta;
	}

	@Override
 	public String toString(){
		return 
			"HomeWorkUpdateResponse{" + 
			"data = '" + data + '\'' + 
			",meta = '" + meta + '\'' + 
			"}";
		}
}