package com.schoolenglishmedium.teacher.model;

import java.util.ArrayList;
import com.google.gson.annotations.SerializedName;

public class AttendanceListData {

	@SerializedName("female_student")
	private ArrayList<FemaleStudentItem> femaleStudent;

	@SerializedName("male_student")
	private ArrayList<MaleStudentItem> maleStudent;

	@SerializedName("taken")
	private String taken;

	public ArrayList<FemaleStudentItem> getFemaleStudent(){
		return femaleStudent;
	}

	public ArrayList<MaleStudentItem> getMaleStudent(){
		return maleStudent;
	}

	public String getTaken() {
		return taken;
	}

	@Override
 	public String toString(){
		return 
			"Data{" + 
			"taken = '" + taken + '\'' +
			"female_student = '" + femaleStudent + '\'' +
			",male_student = '" + maleStudent + '\'' +
			"}";
		}
}