package com.school.teacher.interfaces

import com.school.teacher.model.Homework
import com.school.teacher.model.HomeworkUpdateDetailItem

interface HomeWorkUpdateClickListener {
    fun onViewClicked(homeworkUpdateDetailItem: HomeworkUpdateDetailItem)
    fun onEditClicked(homeworkUpdateDetailItem: HomeworkUpdateDetailItem)
}