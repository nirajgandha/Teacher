package com.schoolenglishmedium.teacher.interfaces

import com.schoolenglishmedium.teacher.model.Syllabus

interface SyllabusClickListener {
    fun onDownloadClicked(syllabus: Syllabus)
    fun onEditClicked(syllabus: Syllabus)
    fun onViewClicked(syllabus: Syllabus)
}