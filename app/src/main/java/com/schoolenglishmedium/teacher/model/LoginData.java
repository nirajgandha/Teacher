package com.schoolenglishmedium.teacher.model;

import com.google.gson.annotations.SerializedName;

public class LoginData{

	@SerializedName("code")
	private String code;

	@SerializedName("updated_at")
	private String updatedAt;

	@SerializedName("phone")
	private String phone;

	@SerializedName("logged_in")
	private String loggedIn;

	@SerializedName("device_token")
	private String deviceToken;

	@SerializedName("created_at")
	private String createdAt;

	@SerializedName("otp")
	private String otp;

	@SerializedName("device_type")
	private String deviceType;

	@SerializedName("id")
	private String id;

	@SerializedName("deleted_at")
	private String deletedAt;

	public String getCode(){
		return code;
	}

	public String getUpdatedAt(){
		return updatedAt;
	}

	public String getPhone(){
		return phone;
	}

	public String getLoggedIn(){
		return loggedIn;
	}

	public String getDeviceToken(){
		return deviceToken;
	}

	public String getCreatedAt(){
		return createdAt;
	}

	public String getOtp(){
		return otp;
	}

	public String getDeviceType(){
		return deviceType;
	}

	public String getId(){
		return id;
	}

	public String getDeletedAt(){
		return deletedAt;
	}

	@Override
 	public String toString(){
		return 
			"LoginData{" + 
			"code = '" + code + '\'' + 
			",updated_at = '" + updatedAt + '\'' + 
			",phone = '" + phone + '\'' + 
			",logged_in = '" + loggedIn + '\'' + 
			",device_token = '" + deviceToken + '\'' + 
			",created_at = '" + createdAt + '\'' + 
			",otp = '" + otp + '\'' + 
			",device_type = '" + deviceType + '\'' + 
			",id = '" + id + '\'' + 
			",deleted_at = '" + deletedAt + '\'' + 
			"}";
		}
}