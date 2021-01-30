package com.school.teacher.model;

import java.util.ArrayList;
import com.google.gson.annotations.SerializedName;

public class SyllabusUpdateDetailData {

	@SerializedName("syllabus_detail")
	private ArrayList<SyllabusDetailItem> syllabusDetail;

	@SerializedName("syllabus_update_detail")
	private ArrayList<SyllabusUpdateDetailItem> syllabusUpdateDetail;

	public ArrayList<SyllabusDetailItem> getSyllabusDetail(){
		return syllabusDetail;
	}

	public ArrayList<SyllabusUpdateDetailItem> getSyllabusUpdateDetail(){
		return syllabusUpdateDetail;
	}

	@Override
 	public String toString(){
		return 
			"Data{" + 
			"syllabus_detail = '" + syllabusDetail + '\'' + 
			",syllabus_update_detail = '" + syllabusUpdateDetail + '\'' + 
			"}";
		}
}