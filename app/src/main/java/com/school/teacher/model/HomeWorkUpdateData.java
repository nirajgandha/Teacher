package com.school.teacher.model;

import java.util.ArrayList;
import com.google.gson.annotations.SerializedName;

public class HomeWorkUpdateData {

	@SerializedName("homework_update_detail")
	private ArrayList<HomeworkUpdateDetailItem> homeworkUpdateDetail;

	@SerializedName("homework_detail")
	private ArrayList<HomeworkDetailItem> homeworkDetail;

	public ArrayList<HomeworkUpdateDetailItem> getHomeworkUpdateDetail(){
		return homeworkUpdateDetail;
	}

	public ArrayList<HomeworkDetailItem> getHomeworkDetail(){
		return homeworkDetail;
	}

	@Override
 	public String toString(){
		return 
			"Data{" + 
			"homework_update_detail = '" + homeworkUpdateDetail + '\'' + 
			",homework_detail = '" + homeworkDetail + '\'' + 
			"}";
		}
}