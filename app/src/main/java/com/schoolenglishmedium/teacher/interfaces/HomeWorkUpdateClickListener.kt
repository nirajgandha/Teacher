package com.schoolenglishmedium.teacher.interfaces

import com.schoolenglishmedium.teacher.model.HomeworkUpdateDetailItem

interface HomeWorkUpdateClickListener {
    fun onViewClicked(homeworkUpdateDetailItem: HomeworkUpdateDetailItem)
    fun onEditClicked(homeworkUpdateDetailItem: HomeworkUpdateDetailItem)
}