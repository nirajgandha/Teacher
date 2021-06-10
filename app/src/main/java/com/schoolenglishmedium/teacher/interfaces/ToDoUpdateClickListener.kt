package com.schoolenglishmedium.teacher.interfaces

import com.schoolenglishmedium.teacher.model.ActivityNotesDetailItem

interface ToDoUpdateClickListener {
    fun onEditClicked(activityNotesDetailItem: ActivityNotesDetailItem)
}