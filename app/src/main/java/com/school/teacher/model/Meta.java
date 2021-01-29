package com.school.teacher.model;

import com.google.gson.annotations.SerializedName;

public class Meta{

	@SerializedName("next")
	private String next;

	@SerializedName("code")
	private String code;

	@SerializedName("message")
	private String message;

	@SerializedName("status")
	private String status;

	public String getNext(){
		return next;
	}

	public String getCode(){
		return code;
	}

	public String getMessage(){
		return message;
	}

	public String getStatus(){
		return status;
	}

	@Override
 	public String toString(){
		return 
			"Meta{" + 
			"next = '" + next + '\'' + 
			",code = '" + code + '\'' + 
			",message = '" + message + '\'' + 
			",status = '" + status + '\'' + 
			"}";
		}
}