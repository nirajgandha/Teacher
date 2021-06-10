package com.schoolenglishmedium.teacher.model

class AttendanceStudentCustomObject(val id: String, val name: String, val grNo: String, val gender: String, var attendanceType: Int, var remark: String){
    override fun toString(): String{
        return "id: $id, name: $name, grNo: $grNo, gender: $gender, attendanceType: $attendanceType, remark: $remark"
    }
}