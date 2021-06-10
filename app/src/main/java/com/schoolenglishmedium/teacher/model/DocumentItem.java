package com.schoolenglishmedium.teacher.model;

import com.google.gson.annotations.SerializedName;

public class DocumentItem{

	@SerializedName("download_link")
	private String downloadLink;

	@SerializedName("original_name")
	private String originalName;

	public String getDownloadLink(){
		return downloadLink;
	}

	public String getOriginalName(){
		return originalName;
	}

	@Override
 	public String toString(){
		return 
			"DocumentItem{" + 
			"download_link = '" + downloadLink + '\'' + 
			",original_name = '" + originalName + '\'' + 
			"}";
		}
}