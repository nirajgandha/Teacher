package com.school.teacher.model;

import java.util.ArrayList;
import com.google.gson.annotations.SerializedName;

public class ToDoActivityResponse{

	@SerializedName("data")
	private ArrayList<ToDoActivityItem> data;

	@SerializedName("meta")
	private Meta meta;

	public ArrayList<ToDoActivityItem> getData(){
		return data;
	}

	public Meta getMeta(){
		return meta;
	}

	@Override
 	public String toString(){
		return 
			"ToDoActivityResponse{" + 
			"data = '" + data + '\'' + 
			",meta = '" + meta + '\'' + 
			"}";
		}
}