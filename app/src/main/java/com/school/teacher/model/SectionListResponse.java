package com.school.teacher.model;

import java.util.ArrayList;
import com.google.gson.annotations.SerializedName;

public class SectionListResponse{

	@SerializedName("data")
	private ArrayList<SectionItem> data;

	@SerializedName("meta")
	private Meta meta;

	public ArrayList<SectionItem> getData(){
		return data;
	}

	public Meta getMeta(){
		return meta;
	}

	@Override
 	public String toString(){
		return 
			"SectionListResponse{" + 
			"data = '" + data + '\'' + 
			",meta = '" + meta + '\'' + 
			"}";
		}
}