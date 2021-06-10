package com.schoolenglishmedium.teacher.interfaces

import com.schoolenglishmedium.teacher.model.Student

interface StudentSelectedListener {
    fun onStudentSelected(student: Student)
}