package com.school.teacher.model;

import java.util.ArrayList;
import java.util.List;
import com.google.gson.annotations.SerializedName;

public class GetAllStudentListData {

	@SerializedName("student_list")
	private ArrayList<StudentListItem> studentList;

	public ArrayList<StudentListItem> getStudentList(){
		return studentList;
	}

	@Override
 	public String toString(){
		return 
			"Data{" + 
			"student_list = '" + studentList + '\'' + 
			"}";
		}
}