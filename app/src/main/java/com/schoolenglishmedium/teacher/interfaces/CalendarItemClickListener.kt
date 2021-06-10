package com.schoolenglishmedium.teacher.interfaces

import com.schoolenglishmedium.teacher.model.CalendarItem

interface CalendarItemClickListener {
    fun onViewClicked(calendarItem: CalendarItem)
    fun onDownloadClicked(calendarItem: CalendarItem)
}