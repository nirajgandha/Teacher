package com.school.teacher.model;

import com.google.gson.annotations.SerializedName;

public class CalendarItem {

	@SerializedName("event_for")
	private String eventFor;

	@SerializedName("show_onwebsite")
	private String showOnwebsite;

	@SerializedName("note")
	private String note;

	@SerializedName("image")
	private String image;

	@SerializedName("from_date")
	private String fromDate;

	@SerializedName("section_name")
	private String sectionName;

	@SerializedName("class_id")
	private String classId;

	@SerializedName("created_at")
	private String createdAt;

	@SerializedName("event_title")
	private String eventTitle;

	@SerializedName("deleted_at")
	private String deletedAt;

	@SerializedName("event_notification_message")
	private String eventNotificationMessage;

	@SerializedName("section_id")
	private String sectionId;

	@SerializedName("to_date")
	private String toDate;

	@SerializedName("updated_at")
	private String updatedAt;

	@SerializedName("id")
	private String id;

	@SerializedName("class_name")
	private String className;

	public String getEventFor(){
		return eventFor;
	}

	public String getShowOnwebsite(){
		return showOnwebsite;
	}

	public String getNote(){
		return note;
	}

	public String getImage(){
		return image;
	}

	public String getFromDate(){
		return fromDate;
	}

	public String getSectionName(){
		return sectionName;
	}

	public String getClassId(){
		return classId;
	}

	public String getCreatedAt(){
		return createdAt;
	}

	public String getEventTitle(){
		return eventTitle;
	}

	public String getDeletedAt(){
		return deletedAt;
	}

	public String getEventNotificationMessage(){
		return eventNotificationMessage;
	}

	public String getSectionId(){
		return sectionId;
	}

	public String getToDate(){
		return toDate;
	}

	public String getUpdatedAt(){
		return updatedAt;
	}

	public String getId(){
		return id;
	}

	public String getClassName(){
		return className;
	}

	@Override
 	public String toString(){
		return 
			"CalendarItem{" +
			"event_for = '" + eventFor + '\'' + 
			",show_onwebsite = '" + showOnwebsite + '\'' + 
			",note = '" + note + '\'' + 
			",image = '" + image + '\'' + 
			",from_date = '" + fromDate + '\'' + 
			",section_name = '" + sectionName + '\'' + 
			",class_id = '" + classId + '\'' + 
			",created_at = '" + createdAt + '\'' + 
			",event_title = '" + eventTitle + '\'' + 
			",deleted_at = '" + deletedAt + '\'' + 
			",event_notification_message = '" + eventNotificationMessage + '\'' + 
			",section_id = '" + sectionId + '\'' + 
			",to_date = '" + toDate + '\'' + 
			",updated_at = '" + updatedAt + '\'' + 
			",id = '" + id + '\'' + 
			",class_name = '" + className + '\'' + 
			"}";
		}
}