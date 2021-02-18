package com.school.teacher.interfaces

import com.school.teacher.model.StudentActivityDetail

interface StudentActivityClickListener {
    fun onViewClicked(studentActivityDetail: StudentActivityDetail)
    fun onEditClicked(studentActivityDetail: StudentActivityDetail)
}