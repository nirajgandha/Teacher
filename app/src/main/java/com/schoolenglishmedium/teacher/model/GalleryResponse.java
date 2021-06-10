package com.schoolenglishmedium.teacher.model;

import java.util.ArrayList;
import com.google.gson.annotations.SerializedName;

public class GalleryResponse{

	@SerializedName("data")
	private ArrayList<GalleryItem> data;

	@SerializedName("meta")
	private Meta meta;

	public ArrayList<GalleryItem> getData(){
		return data;
	}

	public Meta getMeta(){
		return meta;
	}

	@Override
 	public String toString(){
		return 
			"GalleryResponse{" + 
			"data = '" + data + '\'' + 
			",meta = '" + meta + '\'' + 
			"}";
		}
}