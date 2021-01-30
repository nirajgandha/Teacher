package com.school.teacher.model;

import java.util.ArrayList;
import com.google.gson.annotations.SerializedName;

public class TeacherActivityNoteData {

	@SerializedName("activity_notes_detail")
	private ArrayList<ActivityNotesDetailItem> activityNotesDetail;

	@SerializedName("activity_detail")
	private ArrayList<ActivityDetailItem> activityDetail;

	public ArrayList<ActivityNotesDetailItem> getActivityNotesDetail(){
		return activityNotesDetail;
	}

	public ArrayList<ActivityDetailItem> getActivityDetail(){
		return activityDetail;
	}

	@Override
 	public String toString(){
		return 
			"Data{" + 
			"activity_notes_detail = '" + activityNotesDetail + '\'' + 
			",activity_detail = '" + activityDetail + '\'' + 
			"}";
		}
}