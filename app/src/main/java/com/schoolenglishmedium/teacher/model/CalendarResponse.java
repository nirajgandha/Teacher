package com.schoolenglishmedium.teacher.model;

import java.util.ArrayList;
import com.google.gson.annotations.SerializedName;

public class CalendarResponse{

	@SerializedName("data")
	private ArrayList<CalendarItem> data;

	@SerializedName("meta")
	private Meta meta;

	public ArrayList<CalendarItem> getData(){
		return data;
	}

	public Meta getMeta(){
		return meta;
	}

	@Override
 	public String toString(){
		return 
			"CalendarResponse{" + 
			"data = '" + data + '\'' + 
			",meta = '" + meta + '\'' + 
			"}";
		}
}