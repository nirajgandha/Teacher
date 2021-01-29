package com.school.teacher.interfaces

import com.school.teacher.model.Syllabus

interface SyllabusClickListener {
    fun onDownloadClicked(syllabus: Syllabus)
}