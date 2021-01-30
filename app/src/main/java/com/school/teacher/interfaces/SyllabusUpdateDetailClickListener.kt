package com.school.teacher.interfaces

import com.school.teacher.model.ActivityNotesDetailItem
import com.school.teacher.model.Homework
import com.school.teacher.model.HomeworkUpdateDetailItem
import com.school.teacher.model.SyllabusUpdateDetailItem

interface SyllabusUpdateDetailClickListener {
    fun onEditClicked(syllabusUpdateDetailItem: SyllabusUpdateDetailItem)
}