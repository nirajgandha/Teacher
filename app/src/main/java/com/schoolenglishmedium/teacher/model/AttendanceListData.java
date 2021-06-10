package com.schoolenglishmedium.teacher.model;

import java.util.ArrayList;
import com.google.gson.annotations.SerializedName;

public class AttendanceListData {

	@SerializedName("female_student")
	private ArrayList<FemaleStudentItem> femaleStudent;

	@SerializedName("male_student")
	private ArrayList<MaleStudentItem> maleStudent;

	public ArrayList<FemaleStudentItem> getFemaleStudent(){
		return femaleStudent;
	}

	public ArrayList<MaleStudentItem> getMaleStudent(){
		return maleStudent;
	}

	@Override
 	public String toString(){
		return 
			"Data{" + 
			"female_student = '" + femaleStudent + '\'' + 
			",male_student = '" + maleStudent + '\'' + 
			"}";
		}
}