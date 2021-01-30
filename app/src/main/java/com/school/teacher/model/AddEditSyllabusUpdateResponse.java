package com.school.teacher.model;

import com.google.gson.annotations.SerializedName;

public class AddEditSyllabusUpdateResponse{

	@SerializedName("data")
	private AddEditSyllabusUpdateData data;

	@SerializedName("meta")
	private Meta meta;

	public AddEditSyllabusUpdateData getData(){
		return data;
	}

	public Meta getMeta(){
		return meta;
	}

	@Override
 	public String toString(){
		return 
			"AddEditSyllabusUpdateResponse{" + 
			"data = '" + data + '\'' + 
			",meta = '" + meta + '\'' + 
			"}";
		}
}