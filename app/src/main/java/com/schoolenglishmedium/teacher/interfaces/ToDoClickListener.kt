package com.schoolenglishmedium.teacher.interfaces

import com.schoolenglishmedium.teacher.model.ToDoActivityItem

interface ToDoClickListener {
    fun onViewClicked(toDoActivityItem: ToDoActivityItem)
    fun onListClicked(toDoActivityItem: ToDoActivityItem)
}