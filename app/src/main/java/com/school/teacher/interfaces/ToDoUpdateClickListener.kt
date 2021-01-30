package com.school.teacher.interfaces

import com.school.teacher.model.ActivityNotesDetailItem
import com.school.teacher.model.Homework
import com.school.teacher.model.HomeworkUpdateDetailItem

interface ToDoUpdateClickListener {
    fun onEditClicked(activityNotesDetailItem: ActivityNotesDetailItem)
}