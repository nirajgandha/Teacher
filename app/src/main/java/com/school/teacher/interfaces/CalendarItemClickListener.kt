package com.school.teacher.interfaces

import com.school.teacher.model.CalendarItem

interface CalendarItemClickListener {
    fun onViewClicked(calendarItem: CalendarItem)
    fun onDownloadClicked(calendarItem: CalendarItem)
}