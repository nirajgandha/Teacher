package com.school.teacher.model;

import java.util.List;
import com.google.gson.annotations.SerializedName;

public class UpdateHomeworkData {

	@SerializedName("homework_update_detail")
	private List<HomeworkUpdateDetailItem> homeworkUpdateDetail;

	@SerializedName("homework_detail")
	private List<HomeworkDetailItem> homeworkDetail;

	public List<HomeworkUpdateDetailItem> getHomeworkUpdateDetail(){
		return homeworkUpdateDetail;
	}

	public List<HomeworkDetailItem> getHomeworkDetail(){
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