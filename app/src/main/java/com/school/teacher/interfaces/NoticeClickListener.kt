package com.school.teacher.interfaces

import com.school.teacher.model.Notice

interface NoticeClickListener {
    fun onViewClicked(notice: Notice)
}