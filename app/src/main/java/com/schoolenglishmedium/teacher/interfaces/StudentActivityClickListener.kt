package com.schoolenglishmedium.teacher.interfaces

import com.schoolenglishmedium.teacher.model.StudentActivityDetail

interface StudentActivityClickListener {
    fun onViewClicked(studentActivityDetail: StudentActivityDetail)
    fun onEditClicked(studentActivityDetail: StudentActivityDetail)
}