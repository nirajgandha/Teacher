package com.schoolenglishmedium.teacher.customui.materialcalendarview.listeners

import com.schoolenglishmedium.teacher.customui.materialcalendarview.EventDay

/**
 * This interface is used to handle long clicks on calendar cells
 *
 * Created by Applandeo Team.
 */

interface OnDayLongClickListener {
    fun onDayLongClick(eventDay: EventDay)
}