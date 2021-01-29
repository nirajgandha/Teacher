package com.school.teacher.interfaces

import com.school.teacher.model.Homework

interface HomeWorkClickListener {
    fun onViewClicked(homework: Homework)
    fun onDownloadClicked(homework: Homework)
    fun onUploadClicked(homework: Homework)
}