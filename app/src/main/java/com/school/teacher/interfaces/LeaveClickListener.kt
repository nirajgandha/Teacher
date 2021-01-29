package com.school.teacher.interfaces

import com.school.teacher.model.TeacherLeaveData

interface LeaveClickListener {
    fun onEditClicked(teacherLeaveData: TeacherLeaveData)
}
