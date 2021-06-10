package com.schoolenglishmedium.teacher.interfaces

import com.schoolenglishmedium.teacher.model.AttendanceStudentCustomObject

interface AttendanceListener {
    fun onChange(attendanceStudentCustomObject: AttendanceStudentCustomObject)
}