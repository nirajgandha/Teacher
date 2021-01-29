package com.school.teacher.interfaces

import com.school.teacher.model.Student

interface StudentSelectedListener {
    fun onStudentSelected(student: Student)
}