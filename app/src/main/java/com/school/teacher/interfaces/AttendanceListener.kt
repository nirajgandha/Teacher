package com.school.teacher.interfaces

import com.school.teacher.model.AttendanceStudentCustomObject
import com.school.teacher.model.Syllabus

interface AttendanceListener {
    fun onChange(attendanceStudentCustomObject: AttendanceStudentCustomObject)
}