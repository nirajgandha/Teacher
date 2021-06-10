package com.schoolenglishmedium.teacher.interfaces

import com.schoolenglishmedium.teacher.model.SyllabusUpdateDetailItem

interface SyllabusUpdateDetailClickListener {
    fun onEditClicked(syllabusUpdateDetailItem: SyllabusUpdateDetailItem)
}