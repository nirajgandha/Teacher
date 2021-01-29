package com.school.teacher.interfaces

import com.school.teacher.model.ToDoActivityItem

interface ToDoClickListener {
    fun onViewClicked(toDoActivityItem: ToDoActivityItem)
}