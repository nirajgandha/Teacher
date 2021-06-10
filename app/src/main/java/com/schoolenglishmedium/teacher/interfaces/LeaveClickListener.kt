package com.schoolenglishmedium.teacher.interfaces

import com.schoolenglishmedium.teacher.model.TeacherLeaveData

interface LeaveClickListener {
    fun onEditClicked(teacherLeaveData: TeacherLeaveData)
}
