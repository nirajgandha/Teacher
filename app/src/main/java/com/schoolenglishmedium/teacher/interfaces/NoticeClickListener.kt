package com.schoolenglishmedium.teacher.interfaces

import com.schoolenglishmedium.teacher.model.Notice

interface NoticeClickListener {
    fun onViewClicked(notice: Notice)
    fun onEditClicked(notice: Notice)
}