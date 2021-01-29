package com.school.teacher.model;

import java.util.*;

import com.google.gson.annotations.SerializedName;

public class NoticeResponse{

	@SerializedName("data")
	private ArrayList<Notice> data;

	@SerializedName("meta")
	private Meta meta;

	public ArrayList<Notice> getData(){
		return data;
	}

	public Meta getMeta(){
		return meta;
	}

	@Override
 	public String toString(){
		return 
			"NoticeResponse{" + 
			"data = '" + data + '\'' + 
			",meta = '" + meta + '\'' + 
			"}";
		}
}