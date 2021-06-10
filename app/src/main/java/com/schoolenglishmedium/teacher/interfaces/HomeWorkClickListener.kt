package com.schoolenglishmedium.teacher.interfaces

import com.schoolenglishmedium.teacher.model.Homework

interface HomeWorkClickListener {
    fun onViewClicked(homework: Homework)
    fun onDownloadClicked(homework: Homework)
    fun onUploadClicked(homework: Homework)
    fun onEditClicked(homework: Homework)
}