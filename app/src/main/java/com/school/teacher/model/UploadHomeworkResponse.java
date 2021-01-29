package com.school.teacher.model;

import java.util.List;
import com.google.gson.annotations.SerializedName;

public class UploadHomeworkResponse{

	@SerializedName("data")
	private List<HomeworkItem> data;

	@SerializedName("meta")
	private Meta meta;

	public List<HomeworkItem> getData(){
		return data;
	}

	public Meta getMeta(){
		return meta;
	}

	@Override
 	public String toString(){
		return 
			"UploadHomeworkResponse{" + 
			"data = '" + data + '\'' + 
			",meta = '" + meta + '\'' + 
			"}";
		}
}