package com.schoolenglishmedium.teacher.model;

import java.util.*;

import com.google.gson.annotations.SerializedName;

public class FoodMenuResponse{

	@SerializedName("data")
	private ArrayList<FoodMenu> data;

	@SerializedName("meta")
	private Meta meta;

	public ArrayList<FoodMenu> getData(){
		return data;
	}

	public Meta getMeta(){
		return meta;
	}

	@Override
 	public String toString(){
		return 
			"FoodMenuResponse{" + 
			"data = '" + data + '\'' + 
			",meta = '" + meta + '\'' + 
			"}";
		}
}