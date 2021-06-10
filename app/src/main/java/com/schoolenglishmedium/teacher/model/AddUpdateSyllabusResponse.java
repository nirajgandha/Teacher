package com.schoolenglishmedium.teacher.model;

import com.google.gson.annotations.SerializedName;

public class AddUpdateSyllabusResponse{

	@SerializedName("data")
	private AddUpdateSyllabusData data;

	@SerializedName("meta")
	private Meta meta;

	public AddUpdateSyllabusData getData(){
		return data;
	}

	public Meta getMeta(){
		return meta;
	}

	@Override
 	public String toString(){
		return 
			"AddUpdateSyllabusResponse{" + 
			"data = '" + data + '\'' + 
			",meta = '" + meta + '\'' + 
			"}";
		}
}